package ru.omarov.quotes.websocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.websocket.handlers.WebSocketPriceRefreshHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@Slf4j
public class WebSocketClientImpl extends WebSocketClient {

    private final WebSocketPriceRefreshHandler handler;


    public WebSocketClientImpl(URI serverUri,
                               Map<String, String> httpHeaders,
                               WebSocketPriceRefreshHandler handler) {
        super(serverUri, httpHeaders);
        this.handler = handler;
    }


    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("""
                Соединение установлено.
                Код {}.
                """, serverHandshake.getHttpStatus()); // то же самое // сделал
    }

    @Override
    public void onMessage(String s) {
        JSONObject response = new JSONObject(s);
        if (!response.isNull("subscribeCandlesResponse"))
            return;
        Position updatedPosition = handler.refreshPositionNominal(response);
        handler.update(updatedPosition);
        WebSocketMessage<String> message = new TextMessage(
                new JSONObject(Map.of(
                        "uid", updatedPosition.getUid(),
                        "nominal", updatedPosition.getNominal()))
                        .toString()
        );
        try {
            handler.getSession().sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } // убрать этот пиздец в мэсэдж хэндлер // убрал


    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("""
                Соединение закрыто по причине {}.
                Код {}.
                """, reason, code);
    }

    @Override
    public void onError(Exception e) {
        log.error(e.getMessage());
    }
}
