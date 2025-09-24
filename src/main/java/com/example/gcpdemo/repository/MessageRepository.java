package com.example.gcpdemo.repository;

import com.example.gcpdemo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySource(String source);
    List<Message> findByCreatedAtAfter(LocalDateTime dateTime);
    List<Message> findTop10ByOrderByCreatedAtDesc();
}