package com.teixeirafernando.review.collector;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

public abstract class TestContainersConfiguration {

    @Container
    static LocalStackContainer localStack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:3.8.1")
    );

    static final String SQSUrl = "http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000";
    static final String BUCKET_NAME = UUID.randomUUID().toString();
    static final String QUEUE_NAME = UUID.randomUUID().toString();

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("app.bucket", () -> BUCKET_NAME);
        registry.add("app.queue", () -> QUEUE_NAME);
        registry.add(
                "spring.cloud.aws.region.static",
                () -> localStack.getRegion()
        );
        registry.add(
                "spring.cloud.aws.credentials.access-key",
                () -> localStack.getAccessKey()
        );
        registry.add(
                "spring.cloud.aws.credentials.secret-key",
                () -> localStack.getSecretKey()
        );
        registry.add(
                "spring.cloud.aws.s3.endpoint",
                () -> localStack.getEndpointOverride(S3).toString()
        );
        registry.add(
                "spring.cloud.aws.sqs.endpoint",
                () -> localStack.getEndpointOverride(SQS).toString()
        );

        System.setProperty("app.bucket", BUCKET_NAME);
        System.setProperty("app.queue", QUEUE_NAME);
        System.setProperty(
                "spring.cloud.aws.region.static",
                localStack.getRegion()
        );
        System.setProperty(
                "spring.cloud.aws.credentials.access-key",
                localStack.getAccessKey()
        );
        System.setProperty(
                "spring.cloud.aws.credentials.secret-key",
                localStack.getSecretKey()
        );
        System.setProperty(
                "spring.cloud.aws.endpoint",
                localStack.getEndpoint().toString()
        );
    }

    public TestContainersConfiguration() {
        try {
            localStack.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
