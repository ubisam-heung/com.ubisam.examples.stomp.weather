package com.ubisam.boilerplate.stomp.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherApiTests {
	@Mock
	private WeatherApi weatherApi;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testWeatherApiGetWeather() throws Exception {
		JsonNode mockResponse = objectMapper.readTree("{\"temp\":25}");
		when(weatherApi.getWeather()).thenReturn(mockResponse);
		JsonNode result = weatherApi.getWeather();
		assertEquals(25, result.get("temp").asInt());
	}
}
