package com.teixeirafernando.review.collector;

import org.springframework.boot.SpringApplication;

import com.teixeirafernando.review.collector.ReviewCollectorServiceApplication;

public class TestDataUploderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ReviewCollectorServiceApplication::main).with(com.teixeirafernando.review.collector.TestcontainersConfiguration.class).run(args);
	}

}
