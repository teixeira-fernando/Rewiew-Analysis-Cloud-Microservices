package com.teixeirafernando.review.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ReviewCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewCollectorApplication.class, args);
	}

}
