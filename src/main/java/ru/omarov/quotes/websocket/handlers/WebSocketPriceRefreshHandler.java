package ru.omarov.quotes.websocket.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.services.PositionService;
import ru.omarov.quotes.websocket.WebSocketClientImpl;
import ru.omarov.quotes.websocket.requests.SubscribeCandlesRequest;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class WebSocketPriceRefreshHandler extends TextWebSocketHandler {
    WebSocketSession session;

    @Autowired
    private PositionService positionService;
    @Value("${token}")
    private String token;

    @Value("${uri.refresh.price}")
    private String tinkoff_api_uri;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Connection {} opened", session.getId());
        this.session = session;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
            throws URISyntaxException, InterruptedException, JsonProcessingException {
        // пришел мэсэдж
        List<String> uidList = Arrays.stream(message.getPayload().toString().split(",")).toList();
        Map<String, String> headers = Map.of("Authorization", "Bearer " + token);
        URI uri = new URI(tinkoff_api_uri);
        SubscribeCandlesRequest request = new SubscribeCandlesRequest();
        request.setInstruments(uidList);
//        request.getSubscribeCandlesRequest()
//                .setInstruments(uidList
//                        .stream()
//                        .map(PositionUnit::new)
//                        .collect(Collectors.toList()));
        WebSocketClient client = new WebSocketClientImpl(uri, headers, this);
        client.connectBlocking();
        client.send(request.buildJSON().toString());
        //работает вебсокет клиент хэндлер
    } // сделал

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error(exception.getMessage());
        session.close(CloseStatus.SERVER_ERROR);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Connection {} closed with status {}", session.getId(), status.getCode());
    }

    public Position refreshPositionNominal(JSONObject message) {
        Position position = new Position();
        if (!message.keySet().contains("subscribeCandlesResponse")) {
            message = message.getJSONObject("candle");
            position = positionService.getByUid(
                    message.getString("instrumentUid")
            );
            message = message.getJSONObject("close");
            BigDecimal newNominal = BigDecimal.valueOf(message.getInt("units"))
                    .add(
                            BigDecimal.valueOf(message.getInt("nano"), 9));
            position.setNominal(newNominal);
        }
        return position;
    }

    public void update(Position position) {
        positionService.save(position);
    }

    public WebSocketSession getSession() {
        return session;
    }
}
