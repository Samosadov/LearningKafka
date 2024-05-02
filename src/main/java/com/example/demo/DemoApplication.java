package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@EnableKafka
@SpringBootApplication
public class DemoApplication {
	final static String DEMO_TOPICS = "postedmessages";
	final static String DEMO_URI = "post-message";
	final static String DEMO_METHOD = "POST";

	@KafkaListener(topics=DEMO_TOPICS)
	public void msgListener(String msg) {
		System.out.println(msg);
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
