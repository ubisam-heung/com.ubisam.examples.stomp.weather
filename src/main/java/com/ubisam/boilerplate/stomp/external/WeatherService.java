package com.ubisam.boilerplate.stomp.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class WeatherService {
  
    @Autowired
    private WeatherApi weatherApi;

    public JsonNode getAndStoreWeather(String key) {
        JsonNode weather = weatherApi.getWeather();
        WeatherStore.put(key, weather);
        return weather;
    }
}