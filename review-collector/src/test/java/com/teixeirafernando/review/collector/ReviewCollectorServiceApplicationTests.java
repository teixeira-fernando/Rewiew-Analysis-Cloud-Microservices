package com.teixeirafernando.review.collector;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@SpringBootTest
@Testcontainers
class ReviewCollectorServiceApplicationTests {

	@Container
	static LocalStackContainer localStack = new LocalStackContainer(
			DockerImageName.parse("localstack/localstack:3.8.1")
	);

	static final String BUCKET_NAME = UUID.randomUUID().toString();
	static final String QUEUE_NAME = UUID.randomUUID().toString();

	static final String SQSUrl = "http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000";

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
	}

	@BeforeAll
	static void beforeAll() throws IOException, InterruptedException {
		//localStack.execInContainer("awslocal", "s3", "mb", "s3://" + BUCKET_NAME);
		localStack.execInContainer(
				"awslocal",
				"sqs",
				"create-queue",
				"--queue-name",
				QUEUE_NAME
		);
	}

	@Autowired
	ReviewCollectorService reviewCollectorService;

	@Autowired
	ApplicationProperties properties;

	@Test
	void shouldHandleMessageSuccessfully() {
		Review review = new Review(UUID.randomUUID(), UUID.randomUUID(), "Customer Name", "that is the content of my review", 5.0);
		reviewCollectorService.publish(properties.queue(), review);

		await()
				.pollInterval(Duration.ofSeconds(2))
				.atMost(Duration.ofSeconds(10))
				.ignoreExceptions()
				.untilAsserted(() -> {
					JSONObject messageFromSQS = new JSONObject(localStack.execInContainer(
							"awslocal",
							"sqs",
							"receive-message",
							"--queue-url",
							SQSUrl+"/"+QUEUE_NAME
					).getStdout());

					assertThat(messageFromSQS.getJSONArray("Messages").length()).isEqualTo(1);
					assertThat(messageFromSQS.getJSONArray("Messages").getJSONObject(0).get("Body")).isEqualTo(review.toString());
				});
	}

}
