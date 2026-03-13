package com.ubisam.examples.stomp.weather.stomp.ubisam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.u2ware.common.stomp.client.WebsocketStompClient;
import io.u2ware.common.stomp.client.WebsocketStompClientHandler;
import io.u2ware.common.stomp.client.config.WebsocketStompProperties;

@Component
public class WeatherSubscriber implements WebsocketStompClientHandler{

    @Autowired
    public WebsocketStompProperties properties;

    @Autowired
    public ObjectMapper mapper;

    @Override
    public void handleFrame(WebsocketStompClient client, JsonNode message) {
        System.out.println("message: " + message.toString());

        ObjectNode data = mapper.createObjectNode();
        data.put("message", "hello");

        client.send("/app/robot", data);
    }

    @Override
    public String getDestination() {
        return properties.getSubscriptions().get("ubisam");
    }
}
