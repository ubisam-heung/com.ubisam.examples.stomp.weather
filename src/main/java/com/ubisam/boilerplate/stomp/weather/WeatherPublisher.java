package com.ubisam.boilerplate.stomp.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.ubisam.boilerplate.stomp.external.WeatherStore;
import com.ubisam.boilerplate.stomp.external.WeatherConfigProperties;
import com.ubisam.boilerplate.stomp.external.WeatherCronProperties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.u2ware.common.stomp.client.WebsocketStompClient;

@Component
public class WeatherPublisher extends WeatherCronProperties{

  protected Log logger = LogFactory.getLog(getClass());

  @Autowired
  private WebsocketStompClient websocketStompClient;

  @Autowired
  private WeatherConfigProperties config;

  @Scheduled(cron = SEND_WEATHER)
  public void sendWeatherData(){
    JsonNode data = WeatherStore.get(config.getStoreKey());
    logger.info("[sendWeatherData]: "+ data);
    websocketStompClient.send("/app/" + config.getDestination(), data);
  }
  
}
