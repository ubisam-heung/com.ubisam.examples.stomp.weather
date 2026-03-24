package com.ubisam.boilerplate.stomp.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherStoreTests {
	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		WeatherStore.clear();
	}

	@Test
	void testWeatherStorePutGetClear() throws Exception {
		JsonNode value = objectMapper.readTree("{\"humidity\":60}");
		String key = "humidityKey";
		WeatherStore.put(key, value);
		assertEquals(60, WeatherStore.get(key).get("humidity").asInt());
		WeatherStore.clear();
		assertNull(WeatherStore.get(key));
	}
}
