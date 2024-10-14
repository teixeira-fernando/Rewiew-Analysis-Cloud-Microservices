package com.teixeirafernando.review.collector;

import java.util.UUID;

public record Review(UUID id, UUID productId, String customerName, String reviewContent, double rating) {

}
