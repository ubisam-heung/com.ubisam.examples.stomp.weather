package com.ubisam.boilerplate.stomp.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import io.u2ware.common.stomp.client.WebsocketStompClient;
import io.u2ware.common.stomp.client.WebsocketStompClientHandler;
import io.u2ware.common.stomp.client.config.WebsocketStompProperties;
import com.ubisam.boilerplate.stomp.external.WeatherStore;

@Component
public class WeatherSubscriber implements WebsocketStompClientHandler {

    @Autowired
    private WebsocketStompProperties properties;

    @Override
    public void handleFrame(WebsocketStompClient client, JsonNode message) {

        String action = message.has("action") ? message.get("action").asText() : null;
        if(action == null) return;
        
        JsonNode data = WeatherStore.get("weather");
        client.send("/app/weather", data);
    }

    @Override
    public String getDestination() {
        return properties.getSubscriptions().get("weather");
    }


}
