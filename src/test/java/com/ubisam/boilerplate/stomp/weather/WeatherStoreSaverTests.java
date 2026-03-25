package com.ubisam.boilerplate.stomp.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.ubisam.boilerplate.stomp.external.WeatherApi;
import com.ubisam.boilerplate.stomp.external.WeatherConfigProperties;
import com.ubisam.boilerplate.stomp.external.WeatherStore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class WeatherStoreSaverTests {
    @Test
    void getWeatherData_callsFetchAndStoreWeather() throws Exception {
        var saver = Mockito.spy(new WeatherStoreSaver());
        Mockito.doNothing().when(saver).fetchAndStoreWeather();
        saver.getWeatherData();
        Mockito.verify(saver).fetchAndStoreWeather();
    }

    @Test
    void initWeatherData_callsFetchAndStoreWeather() throws Exception {
        var saver = Mockito.spy(new WeatherStoreSaver());
        Mockito.doNothing().when(saver).fetchAndStoreWeather();
        saver.initWeatherData();
        Mockito.verify(saver).fetchAndStoreWeather();
    }

    @Test
    void fetchAndStoreWeather_storesData() throws Exception {
        var saver = new WeatherStoreSaver();
        var weatherApi = Mockito.mock(WeatherApi.class);
        var config = Mockito.mock(WeatherConfigProperties.class);
        var data = Mockito.mock(JsonNode.class);
        Mockito.when(weatherApi.getWeather()).thenReturn(data);
        Mockito.when(config.getStoreKey()).thenReturn("key");
        try (var store = Mockito.mockStatic(WeatherStore.class)) {
            java.lang.reflect.Field f1 = saver.getClass().getDeclaredField("weatherApi");
            java.lang.reflect.Field f2 = saver.getClass().getDeclaredField("config");
            f1.setAccessible(true); f2.setAccessible(true);
            f1.set(saver, weatherApi);
            f2.set(saver, config);
            saver.fetchAndStoreWeather();
            store.verify(() -> WeatherStore.put("key", data));
        }
    }
}

