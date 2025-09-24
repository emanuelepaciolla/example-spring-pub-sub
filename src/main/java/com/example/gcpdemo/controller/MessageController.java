// MessageController.java
package com.example.gcpdemo.controller;

import com.example.gcpdemo.dto.MessageRequest;
import com.example.gcpdemo.dto.MessageResponseTinyDTO;
import com.example.gcpdemo.entity.Message;
import com.example.gcpdemo.service.MessageService;
import com.example.gcpdemo.service.PubSubService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final PubSubService pubSubService;

    @Operation(summary = "Create a new message", description = "Create a new message")
    @PostMapping
    public ResponseEntity<Message> createMessage(@Valid @RequestBody MessageRequest request) {
        Message message = messageService.saveMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @Operation(summary = "Get all messages", description = "Get all messages")
    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @Operation(summary = "Get recent messages", description = "Get recent messages")
    @GetMapping("/recent")
    public List<Message> getRecentMessages() {
        return messageService.getRecentMessages();
    }

    @Operation(summary = "Get message by ID", description = "Get message by ID")
    @GetMapping("/{id}")
    public Message getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id);
    }

    @Operation(summary = "Get message by source", description = "Get message by source")
    @GetMapping("/source/{source}")
    public List<Message> getMessagesBySource(@PathVariable String source) {
        return messageService.getMessagesBySource(source);
    }

    @Operation(summary = "Delete message by ID", description = "Delete message by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
    }

    @Operation(summary = "Publish message to Pub/Sub", description = "Publish message to Pub/Sub")
    @PostMapping("/publish")
    public ResponseEntity<MessageResponseTinyDTO> publishToPubSub(@Valid @RequestBody MessageRequest request) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("source", "REST_API");
        attributes.put("timestamp", String.valueOf(System.currentTimeMillis()));

        String messageId = pubSubService.publishMessage(request.getText(), attributes);

        final var messageResponse = new MessageResponseTinyDTO(messageId, "published");

        return ResponseEntity.ok(messageResponse);
    }
}