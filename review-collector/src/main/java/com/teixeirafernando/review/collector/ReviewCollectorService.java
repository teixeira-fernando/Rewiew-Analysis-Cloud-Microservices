package com.teixeirafernando.review.collector;

import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class ReviewCollectorService {

    private final SqsTemplate sqsTemplate;

    public ReviewCollectorService(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    public SendResult<Review> publish(String queueName, Review review) {
        try {
            return sqsTemplate.send(queueName, review);
        }
        catch (Exception ex){
            throw ex;
        }
    }
}
