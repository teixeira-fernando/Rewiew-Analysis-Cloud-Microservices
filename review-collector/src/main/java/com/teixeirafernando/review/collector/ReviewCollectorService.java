package com.teixeirafernando.review.collector;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewCollectorService {

    private final SqsTemplate sqsTemplate;

    public ReviewCollectorService(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    public void publish(String queueName, Review review) {
        sqsTemplate.send(to -> to.queue(queueName).payload(review));
    }
}
