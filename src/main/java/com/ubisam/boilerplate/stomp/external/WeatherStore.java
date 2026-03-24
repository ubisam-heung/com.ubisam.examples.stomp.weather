package com.ubisam.boilerplate.stomp.external;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class WeatherStore {
    private static final Map<String, JsonNode> store = new ConcurrentHashMap<>();

    public static void put(String key, JsonNode value) {
        store.put(key, value);
    }

    public static JsonNode get(String key) {
        return store.get(key);
    }

    public static void clear() {
        store.clear();
    }
}
