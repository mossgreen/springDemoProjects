package com.serverless.serverlessspringclouddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class ServerlessSpringCloudDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerlessSpringCloudDemoApplication.class, args);
	}

	@Bean
	public Function<String, String> reverseString() {
		return value -> new StringBuilder(value).reverse().toString();
	}
}
