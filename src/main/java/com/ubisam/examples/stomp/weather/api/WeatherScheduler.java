package com.ubisam.examples.stomp.weather.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import io.u2ware.common.stomp.client.WebsocketStompClient;
import io.u2ware.common.stomp.client.config.WebsocketStompProperties;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherScheduler {

	// 임시로 cron을 직접 지정하여 주입
	// 10분마다 실행 (초, 분, 시, 일, 월, 요일)
	protected static final String CALL_WEATHER = "0 0/10 * * * *";
	// 5초마다 실행
	protected static final String SEND_WEATHER = "0/5 * * * * *";
	// 10초마다 실행
	protected static final String CHECK_WEBSOCKET = "0/10 * * * * *";

	@Autowired
  private WebsocketStompProperties properties;

	@Autowired
  private WebsocketStompClient client;

	private final RestTemplate restTemplate = new RestTemplate();
	private final String apiUrl;
	private volatile String latestWeatherData = "{}"; // JSON 문자열로 저장

	public WeatherScheduler(WebsocketStompClient client,
												 @Value("${weather.api.fixed.url}") String apiUrl) {
		this.client = client;
		this.apiUrl = apiUrl;
	}

	// 10분마다 API 호출
	@Scheduled(cron = CALL_WEATHER)
	public void fetchWeather() {
		String response = restTemplate.getForObject(apiUrl, String.class);
		// 필요한 데이터만 추출해서 latestWeatherData에 저장 (여기선 전체 저장)
		latestWeatherData = response;
	}

	// 5초마다 클라이언트로 전송
	@Scheduled(cron = SEND_WEATHER)
	public void sendWeather() {
		//테스트를 위해 직접 /app/weather 설정
		client.send("/app/weather", latestWeatherData);
	}

  // 10초마다 연결상태 체크, 끊어졌으면 재연결
	@Scheduled(cron = CHECK_WEBSOCKET)
	public void checkAndReconnect() {
			if (!client.isConnected()) {
					try {
							client.connect(properties.getSubscriptions().get("weather"));
							System.out.println("WebSocket 재연결 시도");
					} catch (Exception e) {
							System.out.println("WebSocket 재연결 실패: " + e.getMessage());
					}
			}
	}
}
