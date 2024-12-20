package com.teixeirafernando.review.analyzer.integration;

import com.teixeirafernando.review.analyzer.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@ExtendWith({SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest
public class ReviewAnalyzerIntegrationTest extends TestContainersConfiguration {

    @Autowired
    ReviewAnalyzerStorageService reviewAnalyzerStorageService;

    @Autowired
    ReviewAnalyzerMessageListenerService reviewAnalyzerMessageListenerService;

    @Autowired
    ApplicationProperties properties;

    @Test
    void shouldProcessMessagesInTheQueueSuccessfully() throws IOException, InterruptedException, JSONException {
        this.insertTestDataToSQSQueue("""
                {
                    "id": "e921412f-af4b-4b1f-bec4-734982b2fb9c",
                    "productId": "da6037a6-a375-40e2-a8a6-1bb5f9448df0",
                    "customerName": "test",
                    "reviewContent": "test",
                    "rating": 5.0
                }
                """);

        /*await()
                .pollInterval(Duration.ofSeconds(2))
                .atMost(Duration.ofSeconds(10))
                .ignoreExceptions()
                .untilAsserted(() -> {
                            JSONObject messageFromSQS = new JSONObject(localStack.execInContainer(
                                    "awslocal",
                                    "sqs",
                                    "receive-message",
                                    "--queue-url",
                                    SQSUrl + "/" + QUEUE_NAME
                            ).getStdout());
                        });*/

        Thread.sleep(5000);


        boolean bucketExists = this.reviewAnalyzerStorageService.bucketExists(TestContainersConfiguration.BUCKET_NAME);
        boolean reviewExists = this.reviewAnalyzerStorageService.reviewExists(TestContainersConfiguration.BUCKET_NAME, "e921412f-af4b-4b1f-bec4-734982b2fb9c");
        JSONObject review = new JSONObject(this.reviewAnalyzerStorageService.downloadAsString(TestContainersConfiguration.BUCKET_NAME, "e921412f-af4b-4b1f-bec4-734982b2fb9c"));

        assertThat(bucketExists).isTrue();
        assertThat(reviewExists).isTrue();
        assertThat(review.getString("id")).isEqualTo("e921412f-af4b-4b1f-bec4-734982b2fb9c");
        assertThat(review.getString("reviewAnalysis")).isNotNull();

    }
}
