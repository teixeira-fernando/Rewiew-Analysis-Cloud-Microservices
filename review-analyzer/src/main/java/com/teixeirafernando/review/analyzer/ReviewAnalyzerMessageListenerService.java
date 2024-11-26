package com.teixeirafernando.review.analyzer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

@Service
public class ReviewAnalyzerMessageListenerService {

    private final String FILE_FORMAT = ".json";

    private final SqsTemplate sqsTemplate;
    private final ReviewAnalyzerStorageService storageService;
    private final ApplicationProperties properties;

    public ReviewAnalyzerMessageListenerService(
            SqsTemplate sqsTemplate,
            ReviewAnalyzerStorageService storageService,
            ApplicationProperties properties
    ) {
        this.sqsTemplate = sqsTemplate;
        this.storageService = storageService;
        this.properties = properties;
    }

    @SqsListener(queueNames = { "${app.queue}" })
    public void handle(Message sqsMessage) throws JsonProcessingException {
        String bucketName = this.properties.bucket();

        //Optional<Message<?>> sqsMessage = this.sqsTemplate.receive();
        //sqsMessage.get().toString();

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(sqsMessage);
        // De-serialize to an object
        AnalyzedReview analyzedReview = mapper.readValue(sqsMessage.body(), AnalyzedReview.class);
        System.out.println(analyzedReview.getReviewAnalysis()); //John


        String key = analyzedReview.getId().toString();
        ByteArrayInputStream is = new ByteArrayInputStream(
                analyzedReview.toString().getBytes(StandardCharsets.UTF_8)
        );
        this.storageService.upload(bucketName, key+FILE_FORMAT, is);
        System.out.println("Uploaded File "+key+"to bucket "+bucketName);
    }
}
