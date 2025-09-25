package com.example.gcpdemo.service;

import com.example.gcpdemo.dto.MessageRequest;
import com.example.gcpdemo.entity.Message;
import com.example.gcpdemo.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Test
    void saveMessage_CorrectMessage_MessageSaved() {

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setText("message Text");
        messageRequest.setSource("source of the message");

        Message message = new Message();
        message.setText("message Text");
        message.setSource("source of the message");
        message.setId(1L);

        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message savedMessage = messageService.saveMessage(messageRequest);

        assertEquals(message.getId(), savedMessage.getId());
    }

    @Test
    void saveMessage_WithRequest_MessageSaved() {
        MessageRequest request = new MessageRequest();
        request.setText("msg text");
        request.setSource("src");

        Message message = new Message();
        message.setId(2L);
        message.setText("msg text");
        message.setSource("src");

        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message saved = messageService.saveMessage(request);

        assertNotNull(saved);
        assertEquals(2L, saved.getId());
        assertEquals("msg text", saved.getText());
        assertEquals("src", saved.getSource());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void getAllMessages_ReturnsListOfMessages() {
        Message m1 = new Message();
        m1.setId(1L);
        Message m2 = new Message();
        m2.setId(2L);

        when(messageRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Message> result = messageService.getAllMessages();

        assertEquals(2, result.size());
        verify(messageRepository, times(1)).findAll();
    }

    @Test
    void getRecentMessages_ReturnsTop10() {
        Message m = new Message();
        m.setId(5L);

        when(messageRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(List.of(m));

        List<Message> result = messageService.getRecentMessages();

        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getId());
        verify(messageRepository, times(1)).findTop10ByOrderByCreatedAtDesc();
    }

    @Test
    void getMessagesBySource_ReturnsFilteredMessages() {
        Message m1 = new Message();
        m1.setId(10L);
        m1.setSource("API");

        when(messageRepository.findBySource("API")).thenReturn(List.of(m1));

        List<Message> result = messageService.getMessagesBySource("API");

        assertEquals(1, result.size());
        assertEquals("API", result.get(0).getSource());
        verify(messageRepository, times(1)).findBySource("API");
    }

    @Test
    void getMessageById_WhenExists_ReturnsMessage() {
        Message message = new Message();
        message.setId(7L);

        when(messageRepository.findById(7L)).thenReturn(Optional.of(message));

        Message result = messageService.getMessageById(7L);

        assertNotNull(result);
        assertEquals(7L, result.getId());
        verify(messageRepository, times(1)).findById(7L);
    }

    @Test
    void getMessageById_WhenNotExists_ThrowsException() {
        when(messageRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> messageService.getMessageById(99L));

        assertEquals("Message not found with id: 99", ex.getMessage());
        verify(messageRepository, times(1)).findById(99L);
    }

    @Test
    void deleteMessage_CallsRepositoryDelete() {
        Long id = 15L;

        doNothing().when(messageRepository).deleteById(id);

        messageService.deleteMessage(id);

        verify(messageRepository, times(1)).deleteById(id);
    }
}