package com.ubisam.boilerplate.stomp.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.ubisam.boilerplate.stomp.external.WeatherStore;
import com.ubisam.boilerplate.stomp.external.WeatherCronProperties;

import io.u2ware.common.stomp.client.WebsocketStompClient;

@Component
public class WeatherPublisher extends WeatherCronProperties{

  @Autowired
  private WebsocketStompClient websocketStompClient;

  @Scheduled(cron = SEND_WEATHER)
  public void sendWeatherData(){
    JsonNode data = WeatherStore.get("weather");
    websocketStompClient.send("/app/weather", data);
  }
  
}
