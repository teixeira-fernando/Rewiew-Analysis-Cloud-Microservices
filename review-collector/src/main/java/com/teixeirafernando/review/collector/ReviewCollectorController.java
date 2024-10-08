package com.teixeirafernando.review.collector;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public class ReviewCollectorController {

    private final ReviewCollectorService reviewCollectorService;
    private final ApplicationProperties properties;

    ReviewCollectorController(ReviewCollectorService reviewCollectorService, ApplicationProperties properties){
        this.reviewCollectorService = reviewCollectorService;
        this.properties = properties;
    }

    @PostMapping("/api/review")
    public Map<String, String> create(@RequestBody Review review) {
        reviewCollectorService.publish(properties.queue(), review);
        return Map.of("uuid", review.uuid().toString());
    }
}
