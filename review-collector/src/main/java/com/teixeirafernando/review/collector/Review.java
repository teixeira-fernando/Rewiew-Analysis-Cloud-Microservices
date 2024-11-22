package com.teixeirafernando.review.collector;

import java.util.UUID;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

public class Review {

    @Getter final private UUID id;
    @Getter @Setter private UUID productId;
    @Getter @Setter private String customerName;
    @Getter @Setter private String reviewContent;
    @Getter @Setter private double rating;

    public Review(UUID productId, String customerName, String reviewContent, double rating){
        this.id = UUID.randomUUID();
        this.productId = productId;
        this.customerName = customerName;
        this.reviewContent = reviewContent;
        this.rating = rating;
    }

    // Convert to JSON String
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
