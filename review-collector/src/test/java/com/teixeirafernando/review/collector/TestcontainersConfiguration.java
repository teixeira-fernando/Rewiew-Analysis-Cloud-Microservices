package com.teixeirafernando.review.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    public static void main(String[] args) {
        setup();
        SpringApplication.from(ReviewCollectorServiceApplication::main).run(args);
    }

    static void setup() {
        try {
            var container = new LocalStackContainer(
                    DockerImageName.parse("localstack/localstack:3.8.1")
            );
            container.start();

            String BUCKET_NAME = UUID.randomUUID().toString();
            String QUEUE_NAME = UUID.randomUUID().toString();

            container.execInContainer("awslocal", "s3", "mb", "s3://" + BUCKET_NAME);
            container.execInContainer(
                    "awslocal",
                    "sqs",
                    "create-queue",
                    "--queue-name",
                    QUEUE_NAME
            );

            System.setProperty("app.bucket", BUCKET_NAME);
            System.setProperty("app.queue", QUEUE_NAME);
            System.setProperty(
                    "spring.cloud.aws.region.static",
                    container.getRegion()
            );
            System.setProperty(
                    "spring.cloud.aws.credentials.access-key",
                    container.getAccessKey()
            );
            System.setProperty(
                    "spring.cloud.aws.credentials.secret-key",
                    container.getSecretKey()
            );
            System.setProperty(
                    "spring.cloud.aws.endpoint",
                    container.getEndpoint().toString()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
