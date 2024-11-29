package com.teixeirafernando.review.collector;

import io.awspring.cloud.sqs.operations.SendResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class ReviewCollectorController {

    private final ReviewCollectorService reviewCollectorService;
    private final ApplicationProperties properties;

    ReviewCollectorController(ReviewCollectorService reviewCollectorService, ApplicationProperties properties){
        this.reviewCollectorService = reviewCollectorService;
        this.properties = properties;
    }

    @PostMapping("/api/review")
    public ResponseEntity<Review> create(@RequestBody Review review) {
        try {
            reviewCollectorService.publish(properties.queue(), review.toString());

        } catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error when creating a review. Please check the provided input parameter");
        }
        return ResponseEntity.ok(review);
    }
}
