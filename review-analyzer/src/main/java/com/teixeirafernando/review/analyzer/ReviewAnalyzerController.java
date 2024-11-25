package com.teixeirafernando.review.analyzer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

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

    @GetMapping("/api/messages/{uuid}")
    public Map<String, String> get(@PathVariable String id) throws IOException {
        String content = storageService.downloadAsString(properties.bucket(), id);
        return Map.of("id", id, "content", content);
    }
}
