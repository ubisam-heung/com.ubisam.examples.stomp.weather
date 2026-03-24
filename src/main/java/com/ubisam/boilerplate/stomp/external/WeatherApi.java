package com.ubisam.boilerplate.stomp.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(name = "weatherApi", url = "${weather.api.fixed.url}")
public interface WeatherApi {
  @GetMapping
  JsonNode getWeather();
}
