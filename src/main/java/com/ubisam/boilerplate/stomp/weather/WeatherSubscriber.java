package com.ubisam.boilerplate.stomp.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import io.u2ware.common.stomp.client.WebsocketStompClient;
import io.u2ware.common.stomp.client.WebsocketStompClientHandler;
import io.u2ware.common.stomp.client.config.WebsocketStompProperties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ubisam.boilerplate.stomp.external.WeatherKeywordMatcher;
import com.ubisam.boilerplate.stomp.external.WeatherConfigProperties;
import com.ubisam.boilerplate.stomp.external.WeatherStore;

@Component
public class WeatherSubscriber implements WebsocketStompClientHandler {

    protected Log logger = LogFactory.getLog(getClass());

    @Autowired
    private WeatherKeywordMatcher keywordMatcher;

    @Autowired
    private WebsocketStompProperties properties;

    @Autowired
    private WeatherConfigProperties config;

    @Override
    public void handleFrame(WebsocketStompClient client, JsonNode message) {
        JsonNode payload = message.get("payload");
        if (keywordMatcher.containsKeyword(payload, config.getTriggerKeyword())) {
            JsonNode data = WeatherStore.get(config.getStoreKey());
            logger.info("[handleFrame]: "+ data);
            client.send("/app/" + config.getDestination(), data);
        }
    }

    @Override
    public String getDestination() {
        return properties.getSubscriptions().get("weather");
    }


}
