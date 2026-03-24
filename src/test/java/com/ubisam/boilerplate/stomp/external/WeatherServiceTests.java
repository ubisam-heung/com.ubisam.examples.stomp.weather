package com.ubisam.boilerplate.stomp.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherServiceTests {
	@Mock
	private WeatherApi weatherApi;

	@InjectMocks
	private WeatherService weatherService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		WeatherStore.clear();
	}

	@Test
	void testWeatherServiceGetAndStoreWeather() throws Exception {
		JsonNode mockResponse = objectMapper.readTree("{\"temp\":30}");
		when(weatherApi.getWeather()).thenReturn(mockResponse);
		String key = "testKey";
		JsonNode result = weatherService.getAndStoreWeather(key);
		assertEquals(30, result.get("temp").asInt());
		assertEquals(30, WeatherStore.get(key).get("temp").asInt());
	}
}
