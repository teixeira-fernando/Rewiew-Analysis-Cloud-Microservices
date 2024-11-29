package com.teixeirafernando.review.analyzer.integration;

import com.teixeirafernando.review.analyzer.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(
        classes = ReviewAnalyzerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ReviewAnalyzerIntegrationTest extends TestContainersConfiguration {

    @Autowired
    ReviewAnalyzerStorageService reviewAnalyzerStorageService;

    @Autowired
    ReviewAnalyzerMessageListenerService reviewAnalyzerMessageListenerService;

    @Autowired
    ApplicationProperties properties;

    @Test
    void shouldProcessMessagesInTheQueueSuccessfully() throws IOException, InterruptedException {
        this.insertTestDataToSQSQueue("""
                {
                    "id": "e921412f-af4b-4b1f-bec4-734982b2fb9c",
                    "productId": "da6037a6-a375-40e2-a8a6-1bb5f9448df0",
                    "customerName": "test",
                    "reviewContent": "test",
                    "rating": 5.0
                }
                """);

        Thread.sleep(5000);

        this.reviewAnalyzerStorageService.downloadAsString(TestContainersConfiguration.BUCKET_NAME, "e921412f-af4b-4b1f-bec4-734982b2fb9c");

    }
}
