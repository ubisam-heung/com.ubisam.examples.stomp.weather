package com.ubisam.boilerplate.stomp.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.ubisam.boilerplate.stomp.external.WeatherConfigProperties;
import com.ubisam.boilerplate.stomp.external.WeatherStore;
import io.u2ware.common.stomp.client.WebsocketStompClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

class WeatherPublisherTests {
    @Test
    void sendWeatherData_sendsWeather() throws Exception {
        WeatherPublisher publisher = new WeatherPublisher();
        WebsocketStompClient client = Mockito.mock(WebsocketStompClient.class);
        WeatherConfigProperties config = Mockito.mock(WeatherConfigProperties.class);
        JsonNode data = Mockito.mock(JsonNode.class);
        Mockito.when(config.getStoreKey()).thenReturn("key");
        Mockito.when(config.getDestination()).thenReturn("dest");
        try (var store = Mockito.mockStatic(WeatherStore.class)) {
            store.when(() -> WeatherStore.get("key")).thenReturn(data);
            Field f1 = publisher.getClass().getDeclaredField("websocketStompClient");
            Field f2 = publisher.getClass().getDeclaredField("config");
            f1.setAccessible(true); f2.setAccessible(true);
            f1.set(publisher, client);
            f2.set(publisher, config);
            publisher.sendWeatherData();
            Mockito.verify(client).send(Mockito.contains("/app/dest"), Mockito.eq(data));
        }
    }
}
