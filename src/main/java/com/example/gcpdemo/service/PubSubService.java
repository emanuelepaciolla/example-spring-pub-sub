// PubSubService.java
package com.example.gcpdemo.service;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PubSubService {

    private final PubSubTemplate pubSubTemplate;

    private final MessageService messageService;

    @Value("${gcp.pubsub.topic-name}")
    private String topicName;

    public String publishMessage(String message, Map<String, String> attributes) {
        try {
            CompletableFuture<String> future = pubSubTemplate.publish(topicName, message, attributes);
            String messageId = future.get(5, TimeUnit.SECONDS);
            log.info("Published message with ID: {}", messageId);
            return messageId;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Error publishing message to Pub/Sub", e);
            throw new RuntimeException("Failed to publish message", e);
        }
    }

    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public void messageReceiver(Message<?> message) {
        log.info("Message arrived! Payload: {}", message.getPayload());

        MessageHeaders headers = message.getHeaders();
        BasicAcknowledgeablePubsubMessage originalMessage =
                headers.get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);

        try {
            // Process the message
            String payload = (String) message.getPayload();
            messageService.saveMessage(payload, "PUBSUB");

            // Acknowledge the message
            if (originalMessage != null) {
                originalMessage.ack();
                log.info("Message acknowledged");
            }
        } catch (Exception e) {
            log.error("Error processing message", e);
            if (originalMessage != null) {
                originalMessage.nack();
                log.info("Message nacked");
            }
        }
    }
}