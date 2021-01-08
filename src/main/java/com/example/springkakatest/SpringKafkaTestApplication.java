package com.example.springkakatest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@RestController
public class SpringKafkaTestApplication {

	private static final StringBuilder messages = new StringBuilder();

	public static void main(String[] args) throws InterruptedException, IOException {
		SpringApplication.run(SpringKafkaTestApplication.class, args);

		String topic =  System.getenv("KAFKA_TOPIC"); //"spring-kafka-test"

		System.out.println("-----" + topic);

		Producer producer = new Producer(topic);

		new Thread(() -> {
			for(int i=1; i<=100; i++){
				try {
					producer.send("John", "hello " + System.currentTimeMillis());
					TimeUnit.SECONDS.sleep(5);
				} catch (ExecutionException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		Consumer consumer = new Consumer(topic);
		consumer.consumer(
				record ->
						messages.append(String.format("%s : %s </br>", record.key(), record.value()))
		);

		TimeUnit.MINUTES.sleep(5);

		producer.close();
		consumer.close();
	}

	@GetMapping("/messages")
	public String messages() {
		return messages.toString();
	}

	@GetMapping("/health")
	public String health() {
		return "ok";
	}

}
