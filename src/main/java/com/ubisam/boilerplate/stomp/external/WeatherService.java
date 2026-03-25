package com.ubisam.boilerplate.stomp.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service
public class WeatherService {
  
    @Autowired
    private WeatherApi weatherApi;

    protected Log logger = LogFactory.getLog(getClass());

    public JsonNode getAndStoreWeather(String key) {
        JsonNode weather = weatherApi.getWeather();
        logger.info("[getAndStoreWeather]: "+ weather);
        WeatherStore.put(key, weather);
        return weather;
    }
}