package com.ubisam.boilerplate.stomp.external;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class WeatherCronProperties {
  protected static final String GET_WEATHER = "0 0/10 * * * *";
  protected static final String SEND_WEATHER = "0/5 * * * * *";
}
