package ru.omarov.quotes.websocket.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class SubscribeCandlesRequest implements Request{
    private final String subscriptionAction = "SUBSCRIPTION_ACTION_SUBSCRIBE";
    private List<Map<String, String>> instruments;
    private final boolean waitingClose = false;

    public SubscribeCandlesRequest() {
        instruments = new ArrayList<>();
    }

    public JSONObject buildJSON() {
        return new JSONObject(Map.of(getName(), this));
    }

    public void setInstruments(List<String> instrumentList) {
        final String interval = "SUBSCRIPTION_INTERVAL_ONE_MINUTE";
        instrumentList.forEach(uid -> instruments.add(Map.of(
                "interval", interval,
                "instrumentId", uid)));
    }

    private String getName() {
        return "subscribeCandlesRequest";
    }
}




