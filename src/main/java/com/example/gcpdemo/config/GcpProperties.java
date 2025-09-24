package com.example.gcpdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "gcp")
public class GcpProperties {
    
    private String projectId;
    private PubSub pubsub = new PubSub();
    
    @Data
    public static class PubSub {
        private String topicName;
        private String subscriptionName;
        private String emulatorHost;
    }
}