package ru.omarov.quotes.controller;

import jakarta.annotation.PreDestroy;
import org.java_websocket.client.WebSocketClient;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.entity.PositionType;
import ru.omarov.quotes.services.PositionService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/positions")
public class PositionController {
    private final PositionService positionService;
    private CompletableFuture<WebSocketClient> connection;


    public PositionController(PositionService positionService) {

        this.positionService = positionService;
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public Position getByUid(@PathVariable(name = "id") String uid) {
        return positionService.getByUid(uid);
    }

    @CrossOrigin
    @GetMapping("/sectors/{sector}")
    public List<Position> getBySector(@PathVariable("sector") String param) {
        return positionService.getBySector(param);
    }
    @CrossOrigin
    @GetMapping("/exchanges/{exchange}")
    public List<Position> getByExchange(@PathVariable("exchange") String param) {
        return positionService.getByExchange(param);
    }

    @CrossOrigin
    @GetMapping("/types/{type}")
    public Page<Position> getByType(@PathVariable("type") String param,
                                    @RequestParam("p") Integer page,
                                    @RequestParam("el") Integer elements) {
        return positionService.getByType(PositionType.valueOf(param), page, elements);
    }
    @CrossOrigin
    @GetMapping("/currencies/{currency}")
    public List<Position> getByCurrency(@PathVariable("currency") String param) {
        return positionService.getByCurrency(param);
    }

    @CrossOrigin
    @GetMapping("/tickers/{ticker}")
    public List<Position> getByTicker(@PathVariable("ticker") String ticker) {
        return positionService.getByTicker(ticker);
    }
    @CrossOrigin
    @GetMapping("/names/{name}")
    public Position getByName(@PathVariable("name") String param) {
        return positionService.getByName(param);
    }

    @GetMapping("/nominal/{price}")
    public List<Position> getByNominal(@PathVariable("price") String param) {
        return positionService.getByNominal(BigDecimal
                .valueOf(Double.parseDouble(param)));
    }

    @CrossOrigin
    @GetMapping("/get_all")
    public Page<Position> getPositions(@RequestParam("p") Integer page,
                                       @RequestParam("els") Integer elements)
            throws IOException {
        return positionService.getAll(page, elements);
    }

    @CrossOrigin
    @PostMapping("/subscribe")
    public void subscribe(@RequestBody List<String> uids)
            throws URISyntaxException, InterruptedException, ExecutionException {
        if (connection != null) connection.get().closeBlocking();
        connection = positionService.subscribeOnPriceChangeByUid(uids);
    }
    @CrossOrigin
    @PostMapping("/close")
    public void close() throws ExecutionException, InterruptedException {
        if (connection != null) connection.get().closeBlocking();
    }
    @CrossOrigin
    @PreDestroy
    private void destroy() throws ExecutionException, InterruptedException {
        if (connection != null) connection.get().closeBlocking();
    }
}
