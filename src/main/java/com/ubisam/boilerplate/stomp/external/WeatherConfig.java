package com.ubisam.boilerplate.stomp.external;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class WeatherConfig {
  protected String storeKey = "weather";
  protected String triggerKeyword = "action";
  protected String destination = "weather";
}
