package com.teixeirafernando.review.analyzer;

import org.springframework.boot.SpringApplication;

public class TestDataUploderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(DataUploderServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
