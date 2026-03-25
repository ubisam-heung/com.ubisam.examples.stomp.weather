package com.ubisam.boilerplate.stomp.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.ubisam.boilerplate.stomp.external.WeatherConfigProperties;
import com.ubisam.boilerplate.stomp.external.WeatherKeywordMatcher;
import com.ubisam.boilerplate.stomp.external.WeatherStore;
import io.u2ware.common.stomp.client.WebsocketStompClient;
import io.u2ware.common.stomp.client.config.WebsocketStompProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import org.mockito.Mockito;

class WeatherSubscriberTests {
    @Test
    void handleFrame_keywordMatched_sendsData() throws Exception {
        WeatherKeywordMatcher keywordMatcher = Mockito.mock(WeatherKeywordMatcher.class);
        WebsocketStompProperties properties = Mockito.mock(WebsocketStompProperties.class);
        WeatherConfigProperties config = Mockito.mock(WeatherConfigProperties.class);
        WebsocketStompClient client = Mockito.mock(WebsocketStompClient.class);
        JsonNode payload = Mockito.mock(JsonNode.class);
        JsonNode message = Mockito.mock(JsonNode.class);
        Mockito.when(message.get("payload")).thenReturn(payload);
        Mockito.when(keywordMatcher.containsKeyword(payload, "trigger")).thenReturn(true);
        Mockito.when(config.getTriggerKeyword()).thenReturn("trigger");
        Mockito.when(config.getStoreKey()).thenReturn("key");
        Mockito.when(config.getDestination()).thenReturn("dest");
        JsonNode data = Mockito.mock(JsonNode.class);
        try (var store = Mockito.mockStatic(WeatherStore.class)) {
            store.when(() -> WeatherStore.get("key")).thenReturn(data);
            WeatherSubscriber subscriber = new WeatherSubscriber();
            Field f1 = subscriber.getClass().getDeclaredField("keywordMatcher");
            Field f2 = subscriber.getClass().getDeclaredField("properties");
            Field f3 = subscriber.getClass().getDeclaredField("config");
            f1.setAccessible(true); f2.setAccessible(true); f3.setAccessible(true);
            f1.set(subscriber, keywordMatcher);
            f2.set(subscriber, properties);
            f3.set(subscriber, config);
            subscriber.handleFrame(client, message);
            Mockito.verify(client).send(Mockito.contains("/app/dest"), Mockito.eq(data));
        }
    }

    @Test
    void handleFrame_keywordNotMatched_doesNotSend() throws Exception {
        WeatherKeywordMatcher keywordMatcher = Mockito.mock(WeatherKeywordMatcher.class);
        WeatherConfigProperties config = Mockito.mock(WeatherConfigProperties.class);
        WebsocketStompClient client = Mockito.mock(WebsocketStompClient.class);
        JsonNode message = Mockito.mock(JsonNode.class);
        JsonNode payload = Mockito.mock(JsonNode.class);
        Mockito.when(message.get("payload")).thenReturn(payload);
        Mockito.when(keywordMatcher.containsKeyword(payload, "trigger")).thenReturn(false);
        Mockito.when(config.getTriggerKeyword()).thenReturn("trigger");
        WeatherSubscriber subscriber = new WeatherSubscriber();
        Field f1 = subscriber.getClass().getDeclaredField("keywordMatcher");
        Field f3 = subscriber.getClass().getDeclaredField("config");
        f1.setAccessible(true); f3.setAccessible(true);
        f1.set(subscriber, keywordMatcher);
        f3.set(subscriber, config);
        subscriber.handleFrame(client, message);
        Mockito.verify(client, Mockito.never()).send(Mockito.anyString(), Mockito.any());
    }

    @Test
    void getDestination_returnsWeatherSubscription() {
        var properties = Mockito.mock(WebsocketStompProperties.class);
        java.util.Map<String, String> map = new java.util.HashMap<>();
        map.put("weather", "dest");
        Mockito.when(properties.getSubscriptions()).thenReturn(map);
        var subscriber = new WeatherSubscriber();
        try {
            var f2 = subscriber.getClass().getDeclaredField("properties");
            f2.setAccessible(true);
            f2.set(subscriber, properties);
        } catch (Exception e) { throw new RuntimeException(e); }
        assertEquals("dest", subscriber.getDestination());
    }
}

