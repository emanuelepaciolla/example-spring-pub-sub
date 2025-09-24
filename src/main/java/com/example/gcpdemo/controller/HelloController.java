// HelloController.java
package com.example.gcpdemo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    
    @Value("${spring.application.name}")
    private String applicationName;
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
    
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", applicationName);
        response.put("message", "Welcome to GCP Spring Boot Demo");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "healthy");
        return response;
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", applicationName);
        return response;
    }
}