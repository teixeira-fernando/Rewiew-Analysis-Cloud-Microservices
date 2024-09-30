package com.teixeirafernando.review.collector;

import org.springframework.boot.SpringApplication;

public class TestDataUploderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ReviewCollectorServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
