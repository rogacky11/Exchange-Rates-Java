package org.example;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import org.json.simple.JSONObject;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRate {
    private long timestamp;
    private Map<String, Double> rates = new HashMap<>();

    public ExchangeRate(JSONObject jsonObject) {
        this.timestamp = (long) jsonObject.get("timestamp");
        JSONObject ratesObject = (JSONObject) jsonObject.get("rates");
        for (Object key : ratesObject.keySet()) {
            String keyStr = (String) key;
            Double keyValue = ((Number) ratesObject.get(keyStr)).doubleValue();
            this.rates.put(keyStr, keyValue);
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public String getFormattedDate() {
        Instant instant = Instant.ofEpochSecond(timestamp);
        return DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.systemDefault())
                .format(instant);
    }
}

