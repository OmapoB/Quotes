package ru.omarov.quotes.websocket.requests.refrescandle;

import lombok.Data;

import java.util.List;

@Data
public class RequestParams {
    private String subscriptionAction = "SUBSCRIPTION_ACTION_SUBSCRIBE";
    private List<PositionUnit> instruments;
    private boolean waitingClose = false;
}