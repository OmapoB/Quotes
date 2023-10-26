package ru.omarov.quotes.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.services.PositionService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;


public class WebSocketClientImpl extends WebSocketClient {

    private PositionService positionService;

    private static final Logger LOGGER =
            LoggerFactory.getLogger(WebSocketClientImpl.class);

    public WebSocketClientImpl(URI serverUri,
                               Map<String, String> httpHeaders,
                               PositionService positionService) {
        super(serverUri, httpHeaders);
        this.positionService = positionService;
    }


    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LOGGER.info("Соединение установлено");
    }

    @Override
    public void onMessage(String s) {
        System.out.println(s);
        if (!new JSONObject(s).keySet().contains("subscribeCandlesResponse")) {
            JSONObject response = new JSONObject(s).getJSONObject("candle");
            Position position = positionService.getByUid(response
                    .getString("instrumentUid"));
            response = response.getJSONObject("close");
            BigDecimal newNominal = BigDecimal.valueOf(response
                            .getInt("units"))
                    .add(BigDecimal.valueOf(response
                            .getInt("nano"), 9));
            position.setNominal(newNominal);
            try {
                positionService.updateOrCreate(position);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        LOGGER.info("Соединение закрыто");
    }

    @Override
    public void onError(Exception e) {
        LOGGER.error(e.getMessage());
    }
}
