package ru.omarov.quotes.services;

import com.google.protobuf.GeneratedMessageV3;
import org.java_websocket.client.WebSocketClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.entity.PositionType;
import ru.omarov.quotes.repositories.PositionRepo;
import ru.omarov.quotes.websocket.WebSocketClientImpl;
import ru.omarov.quotes.websocket.handlers.MyHandler;
import ru.omarov.quotes.websocket.requests.refrescandle.PositionUnit;
import ru.omarov.quotes.websocket.requests.refrescandle.SubscribeCandlesRequest;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class PositionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PositionService.class);
    private static final Integer MAX_POSITIONS_ON_PAGE = 300;

    @Value("${token}")
    private String token;
    private final Environment env;

    private PositionRepo repos;

    private MyHandler handler;


    public PositionService(Environment env, PositionRepo repos, MyHandler handler) {
        this.env = env;
        this.repos = repos;
        this.handler = handler;
    }


    @Transactional
    public <T extends GeneratedMessageV3> Position updateOrCreate(T position) {
        Position newPosition = new Position();

        newPosition.setType(Arrays.stream(PositionType.values())
                .filter(s -> s.getaClass() == position.getClass())
                .findFirst().orElse(null));
        newPosition
                .setTicker(position.getField(position.getDescriptorForType()
                        .findFieldByName("ticker")).toString());
        newPosition
                .setClassCode(position.getField(position.getDescriptorForType()
                        .findFieldByName("class_code")).toString());
        newPosition
                .setLot(Integer.parseInt(position.getField(position.getDescriptorForType()
                        .findFieldByName("lot")).toString()));
        newPosition
                .setCurrency(position.getField(position.getDescriptorForType()
                        .findFieldByName("currency")).toString());
        newPosition
                .setName(position.getField(position.getDescriptorForType()
                        .findFieldByName("name")).toString());
        newPosition
                .setExchange(position.getField(position.getDescriptorForType()
                        .findFieldByName("exchange")).toString());
        newPosition
                .setSector(position.getField(position.getDescriptorForType()
                        .findFieldByName("sector")).toString());

        MoneyValue moneyValue = (MoneyValue) position.getField(position.getDescriptorForType()
                .findFieldByName("nominal"));
        newPosition
                .setNominal(moneyValue.getUnits() == 0 && moneyValue.getNano() == 0 ?
                        BigDecimal.ZERO : BigDecimal.valueOf(moneyValue.getUnits())
                        .add(BigDecimal.valueOf(moneyValue.getNano(), 9)));

        Quotation quotation = (Quotation) position.getField(position.getDescriptorForType()
                .findFieldByName("min_price_increment"));
        newPosition
                .setMinPriceIncrement(quotation.getUnits() == 0 && quotation.getNano() == 0 ?
                        BigDecimal.ZERO : BigDecimal.valueOf(quotation.getUnits())
                        .add(BigDecimal.valueOf(quotation.getNano(), 9)));
        newPosition
                .setUid(position.getField(position.getDescriptorForType()
                        .findFieldByName("uid")).toString());
        return repos.save(newPosition);
    }

    public Position updateOrCreate(Position position) throws IOException {
        WebSocketMessage<String> message = new TextMessage(
                new JSONObject(Map.of(
                        "uid", position.getUid(),
                        "nominal", position.getNominal()))
                        .toString()
        );
        handler.getSession().sendMessage(message);
        return repos.save(position);
    }

    public Page<Position> getAll(Integer page, Integer elements) {
        if (page == null) page = 0;
        if (elements == null) elements = MAX_POSITIONS_ON_PAGE;
        if (elements > MAX_POSITIONS_ON_PAGE)
            throw new IllegalArgumentException("Количество элементов на странице не должно превышать "
                    + MAX_POSITIONS_ON_PAGE);
        return repos.findAll(PageRequest.of(page, elements));
    }

    public Page<Position> getByType(PositionType type, Integer page, Integer elements) {

        return repos.findByType(type, PageRequest.of(page, elements));
    }

    public Position getByUid(String uid) {
        return repos.findByUid(uid);
    }

    public Position getByName(String name) {
        return repos.findByName(name);
    }

    public List<Position> getByTicker(String ticker) {
        return repos.findByTicker(ticker);
    }

    public List<Position> getByCurrency(String currency) {
        return repos.findByCurrency(currency);
    }

    public List<Position> getByExchange(String exchange) {
        return repos.findByExchange(exchange);
    }

    public List<Position> getBySector(String sector) {
        return repos.findBySector(sector);
    }

    public List<Position> getByNominal(BigDecimal price) {
        return repos.findByNominal(price);
    }

    public Map<String, BigDecimal> getNominalListByUidList(List<String> uids) {
        Map<String, BigDecimal> nominalList = new HashMap<>();
        uids.forEach(uid -> nominalList.put(uid, repos.findByUid(uid).getNominal()));
        return nominalList;
    }

    @Async
    public CompletableFuture<WebSocketClient> subscribeOnPriceChangeByUid(List<String> uids)
            throws URISyntaxException, InterruptedException {
        System.out.println("________________________________________________________");
        Map<String, String> headers = Map.of("Authorization", "Bearer " + token);
        URI uri = new URI(Objects.requireNonNull(env.getProperty("uri.refresh.price")));
        SubscribeCandlesRequest message = new SubscribeCandlesRequest();
        message.getSubscribeCandlesRequest()
                .setInstruments(uids
                        .stream()
                        .map(PositionUnit::new)
                        .collect(Collectors.toList()));
        WebSocketClient client = new WebSocketClientImpl(uri, headers, this);
        System.out.println(message);
        client.connectBlocking();
        client.send(message.toString());
        return CompletableFuture.completedFuture(client);
    }
}
