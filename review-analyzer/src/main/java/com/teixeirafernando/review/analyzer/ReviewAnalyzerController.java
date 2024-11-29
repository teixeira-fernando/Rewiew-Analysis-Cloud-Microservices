package com.teixeirafernando.review.analyzer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ReviewAnalyzerController {

    private final ReviewAnalyzerStorageService storageService;
    private final ApplicationProperties properties;

    public ReviewAnalyzerController(
            ReviewAnalyzerStorageService storageService,
            ApplicationProperties properties
    ) {
        this.storageService = storageService;
        this.properties = properties;
    }

    @GetMapping(value = "/api/messages/{id}",  produces = { "application/json" })
    public String get(@PathVariable String id) throws IOException {
        return storageService.downloadAsString(properties.bucket(), id);
    }
}
