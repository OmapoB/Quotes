package ru.omarov.quotes.websocket.requests.refrescandle;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PositionUnit {
    private final String interval = "SUBSCRIPTION_INTERVAL_ONE_MINUTE";
    private String instrumentId;

}