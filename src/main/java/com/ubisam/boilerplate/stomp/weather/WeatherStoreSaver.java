package com.ubisam.boilerplate.stomp.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ubisam.boilerplate.stomp.external.WeatherApi;
import com.ubisam.boilerplate.stomp.external.WeatherConfigProperties;
import com.ubisam.boilerplate.stomp.external.WeatherStore;

import jakarta.annotation.PostConstruct;

import com.ubisam.boilerplate.stomp.external.WeatherCronProperties;

@Component
public class WeatherStoreSaver extends WeatherCronProperties{

  @Autowired
  private WeatherApi weatherApi;

  @Autowired
  private WeatherConfigProperties config;

  @Scheduled(cron = GET_WEATHER)
  public void getWeatherData(){
    fetchAndStoreWeather();
  }

  @PostConstruct
  public void initWeatherData() {
    fetchAndStoreWeather();
  }

  void fetchAndStoreWeather() {
    var data = weatherApi.getWeather();
    WeatherStore.put(config.getStoreKey(), data);
  }

}
