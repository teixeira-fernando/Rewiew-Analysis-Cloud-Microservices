package com.teixeirafernando.review.collector.integration;

import com.teixeirafernando.review.collector.Review;
import com.teixeirafernando.review.collector.ReviewCollectorController;
import com.teixeirafernando.review.collector.TestContainersConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@WebMvcTest(ReviewCollectorController.class)
public class ReviewCollectorIntegrationTest extends TestContainersConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /api/review - Success")
    void testSuccessfulNewReviewCreation() throws Exception {

        // Arrange
        Review review = new Review(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Customer Name",
                "This is a review content",
                5.0
        );

        // Act & Assert
        mockMvc.perform(post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(review.toString())))
                .andExpect(status().isOk()) //validate 200 response code
                .andExpect(jsonPath("$.id").value(review.id().toString())); //validate response body

    }
}
