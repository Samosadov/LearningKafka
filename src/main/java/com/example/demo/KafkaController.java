package com.example.demo;

//import org.apache.tomcat.util.json.JSONParser;
//import org.apache.tomcat.util.json.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.example.demo.DemoApplication.*;

@RestController
@RequestMapping(DEMO_URI)
public class KafkaController {
//    private final KafkaProducerService producerService;
    private String msg_id;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

//    public KafkaController(KafkaProducerService producerService) {
//        this.producerService = producerService;
//    }

    @PostMapping
    public String sendMessageToKafka(@RequestBody String message) {
        StringBuilder msgToKafka = new StringBuilder("{ ");
        try {
            Object obj = new JSONParser().parse(message);
            JSONObject jo = (JSONObject) obj;
            msg_id = (String) jo.get("msg_id");

            msgToKafka.append("\"msg_id\": \"").append(msg_id).append("\", ")
                    .append("\"timestamp\": \"").append(Instant.now().toEpochMilli()).append("\", ")
                    .append("\"method\": \"").append(DEMO_METHOD).append("\", ")
                    .append("\"uri\": \"/").append(DEMO_URI).append("\"");

        } catch (ParseException e) {
            System.out.println("Error of parsing");;
        }
        msgToKafka.append(" }");

//        producerService.sendMessage(msgToKafka.toString());
        CompletableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(DEMO_TOPICS, msgToKafka.toString());

        try {
            System.out.println(future.get().toString());
        } catch (InterruptedException | ExecutionException e) {
            return "Error 500";
        }
        return "200 OK";
    }
}