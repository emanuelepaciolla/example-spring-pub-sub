// MessageController.java
package com.example.gcpdemo.controller;

import com.example.gcpdemo.dto.MessageRequest;
import com.example.gcpdemo.entity.Message;
import com.example.gcpdemo.service.MessageService;
import com.example.gcpdemo.service.PubSubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    
    private final MessageService messageService;
    private final PubSubService pubSubService;
    
    @PostMapping
    public ResponseEntity<Message> createMessage(@Valid @RequestBody MessageRequest request) {
        Message message = messageService.saveMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
    
    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }
    
    @GetMapping("/recent")
    public List<Message> getRecentMessages() {
        return messageService.getRecentMessages();
    }
    
    @GetMapping("/{id}")
    public Message getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id);
    }
    
    @GetMapping("/source/{source}")
    public List<Message> getMessagesBySource(@PathVariable String source) {
        return messageService.getMessagesBySource(source);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
    }
    
    @PostMapping("/publish")
    public ResponseEntity<Map<String, String>> publishToPubSub(@Valid @RequestBody MessageRequest request) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("source", "REST_API");
        attributes.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        String messageId = pubSubService.publishMessage(request.getText(), attributes);
        
        Map<String, String> response = new HashMap<>();
        response.put("messageId", messageId);
        response.put("status", "published");
        
        return ResponseEntity.ok(response);
    }
}