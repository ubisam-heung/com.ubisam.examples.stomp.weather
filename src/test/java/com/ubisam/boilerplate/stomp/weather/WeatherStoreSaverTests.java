package com.ubisam.boilerplate.stomp.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.ubisam.boilerplate.stomp.external.WeatherApi;
import com.ubisam.boilerplate.stomp.external.WeatherConfigProperties;
import com.ubisam.boilerplate.stomp.external.WeatherStore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

class WeatherStoreSaverTests {
    @Test
    void getWeatherData_callsFetchAndStoreWeather() {
        var saver = Mockito.spy(new WeatherStoreSaver());
        Mockito.doNothing().when(saver).fetchAndStoreWeather();
        saver.getWeatherData();
        Mockito.verify(saver).fetchAndStoreWeather();
    }

    @Test
    void initWeatherData_callsFetchAndStoreWeather() {
        var saver = Mockito.spy(new WeatherStoreSaver());
        Mockito.doNothing().when(saver).fetchAndStoreWeather();
        saver.initWeatherData();
        Mockito.verify(saver).fetchAndStoreWeather();
    }

    @Test
    void fetchAndStoreWeather_storesData() throws Exception {
        var saver = new WeatherStoreSaver();
        WeatherApi weatherApi = Mockito.mock(WeatherApi.class);
        WeatherConfigProperties config = Mockito.mock(WeatherConfigProperties.class);
        JsonNode data = Mockito.mock(JsonNode.class);
        Mockito.when(weatherApi.getWeather()).thenReturn(data);
        Mockito.when(config.getStoreKey()).thenReturn("key");
        try (var store = Mockito.mockStatic(WeatherStore.class)) {
            Field f1 = saver.getClass().getDeclaredField("weatherApi");
            Field f2 = saver.getClass().getDeclaredField("config");
            f1.setAccessible(true); f2.setAccessible(true);
            f1.set(saver, weatherApi);
            f2.set(saver, config);
            saver.fetchAndStoreWeather();
            store.verify(() -> WeatherStore.put("key", data));
        }
    }
}

