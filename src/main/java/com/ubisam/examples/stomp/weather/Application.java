package com.ubisam.examples.stomp.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling //2026.03.13 - 스케줄링 어노테이션을 활용하기 위해 추가
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
