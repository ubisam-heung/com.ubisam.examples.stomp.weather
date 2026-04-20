# Ubisam STOMP Weather (Java/Spring Boot)

---

## 개요
Spring Boot 기반의 STOMP(WebSocket) 통신과 외부 날씨 API(OpenWeatherMap) 연동 예제입니다. 실시간 날씨 정보를 주기적으로 수집·저장하고, 키워드 기반 트리거로 최신 데이터를 STOMP로 발행합니다.

---

## ✨ 주요 기능
- **STOMP 실시간 통신:** WebSocket 기반 STOMP 프로토콜로 토픽 구독 및 메시지 송수신
- **외부 날씨 API 연동:** OpenWeatherMap API에서 실시간 날씨 데이터 수집
- **자동 데이터 저장:** 수집한 날씨 정보를 메모리 저장소에 보관
- **키워드 기반 트리거:** 특정 키워드 메시지 수신 시 최신 날씨 데이터 자동 발행
- **DI 및 컴포넌트 분리:** 서비스, 저장소, 설정, 퍼블리셔, 서브스크라이버 등 역할별 컴포넌트 구조

---

## 🗂️ 폴더 구조
```
src/
	main/
		java/com/ubisam/boilerplate/
			Application.java                  # Spring Boot 진입점
			ApplicationFeignConfig.java       # FeignClient 설정
			ApplicationScheduleConfig.java    # 스케줄러 설정
			ApplicationStompConfig.java       # STOMP/WebSocket 설정
			stomp/
				external/
					WeatherApi.java              # 외부 날씨 API 연동 (FeignClient)
					WeatherConfigProperties.java # 날씨 관련 설정값 관리
					WeatherCronProperties.java   # 스케줄/크론 설정
					WeatherService.java          # 날씨 데이터 수집 및 저장 서비스
					WeatherStore.java            # 메모리 기반 날씨 데이터 저장소
				util/
					WeatherKeywordMatcher.java   # 키워드 감지 유틸
				weather/
					WeatherPublisher.java        # 주기적 데이터 발행 컴포넌트
					WeatherStoreSaver.java       # 날씨 데이터 저장 트리거
					WeatherSubscriber.java       # 키워드 감지 후 데이터 발행 핸들러
		resources/
			application.properties           # 환경설정 파일
	test/
		java/com/ubisam/boilerplate/
			ApplicationTests.java
			stomp/external/                  # 외부 연동 단위 테스트
			stomp/util/                  		 # 외부 유틸 단위 테스트
			stomp/weather/                   # 퍼블리셔/서브스크라이버 테스트
```

---

## ⚙️ 환경설정 (application.properties)
```properties
spring.application.name=weather

# STOMP 설정
io.u2ware.common.stomp.url=ws://localhost:9030/stomp
io.u2ware.common.stomp.subscriptions.weather=/topic/weather

# 날씨 API 설정
weather.api.key=...
weather.api.base.url=...
weather.api.fixed.url=...
```

---


## 💻 실행 방법

### 🛠️ 개발환경 (로컬 개발)

1. **Docker Compose로 개발환경 컨테이너 실행**
	```bash
	docker compose -f Dockerfile-dev.yml up -d
	```

2. **Spring Boot 애플리케이션 실행**
	```bash
	./mvnw spring-boot:run
	```

3. **접속**
	- http://localhost:9030

---

### 🚀 빌드/운영환경 (배포용)

1. **패키징**
	```bash
	./mvnw package
	```

2. **멀티플랫폼 Docker 이미지 빌드**
	```bash
	docker build --platform linux/arm64,linux/amd64 -t weather-boilerplate/backend:0.0.1-SNAPSHOT .
	```

3. **Docker Compose로 운영환경 컨테이너 실행**
	```bash
	docker compose -f Dockerfile-run.yml up -d
	```

4. **접속**
	- http://localhost:9030

---


## 🧩 주요 클래스 및 역할

- **Application** : Spring Boot 실행 진입점
- **WeatherApi** : 외부 날씨 API 연동 (FeignClient)
- **WeatherService** : 날씨 데이터 수집 및 WeatherStore 저장
- **WeatherStore** : 메모리 기반 데이터 저장/조회/초기화
- **WeatherConfigProperties** : 발행/트리거 관련 설정값 관리

---

## ⚡ 커스터마이즈 안내

`WeatherConfigProperties` 클래스의 storeKey, triggerKeyword, destination 값은 원하는 값으로 자유롭게 변경할 수 있습니다.
예시)

```java
@Component
@Data
public class WeatherConfigProperties {
	protected String storeKey = "weather";           // 저장소 키 (원하는 값으로 변경 가능)
	protected String triggerKeyword = "action";      // 트리거 키워드 (원하는 값으로 변경 가능)
	protected String destination = "weather";        // 발행 대상 (원하는 값으로 변경 가능)
}
```

이 값들을 프로젝트 목적에 맞게 수정하여 사용할 수 있습니다.
- **WeatherSubscriber** : STOMP 메시지 수신 후 키워드 감지 및 데이터 발행
- **WeatherPublisher** : 주기적 데이터 발행 (스케줄 기반)
- **WeatherStoreSaver** : 날씨 데이터 저장 트리거

---

## 📝 사용 예시 (핵심 로직)
```java
// 날씨 데이터 수집 및 저장
JsonNode weather = weatherApi.getWeather();
WeatherStore.put("weather", weather);

// STOMP 메시지 수신 시 키워드 감지 후 데이터 발행
if (keywordMatcher.containsKeyword(payload, config.getTriggerKeyword())) {
		JsonNode data = WeatherStore.get(config.getStoreKey());
		client.send("/app/" + config.getDestination(), data);
}
```

---

## 🧪 테스트
- JUnit 기반 단위 테스트: `src/test/java/com/ubisam/boilerplate/`
- WeatherService, WeatherApi, WeatherStore, WeatherPublisher 등 주요 컴포넌트 테스트 포함

---

## 🛠️ 의존성 및 지원 환경
- Java 17 이상
- Spring Boot 3.5.x
- 주요 의존성: spring-boot-starter-websocket, spring-boot-starter-data-jpa, spring-cloud-starter-openfeign, u2ware-common-stomp 등

---

## 📢 참고
- 실제 서비스 연동 시, `application.properties`의 API Key 및 STOMP 서버 주소를 환경에 맞게 수정하세요.
- WeatherPublisher의 주기적 발행 기능은 스케줄 설정에 따라 동작합니다.

---

