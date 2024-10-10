package com.teixeirafernando.review.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ReviewCollectorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewCollectorServiceApplication.class, args);
	}

}
