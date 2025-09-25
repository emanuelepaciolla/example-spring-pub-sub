package com.example.gcpdemo.service;

import com.example.gcpdemo.dto.MessageRequest;
import com.example.gcpdemo.entity.Message;
import com.example.gcpdemo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public Message saveMessage(String text, String source) {
        Message message = new Message();
        message.setText(text);
        message.setSource(source);

        Message saved = messageRepository.save(message);
        log.info("Saved message with ID: {}", saved.getId());
        return saved;
    }

    @Transactional
    public Message saveMessage(MessageRequest request) {
        return saveMessage(request.getText(), request.getSource());
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getRecentMessages() {
        return messageRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public List<Message> getMessagesBySource(String source) {
        return messageRepository.findBySource(source);
    }

    public Message getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
    }

    @Transactional
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
        log.info("Deleted message with ID: {}", id);
    }
}