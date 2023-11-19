package ru.omarov.quotes.websocket.requests;

import org.json.JSONObject;

public interface Request {
    JSONObject buildJSON();
}
