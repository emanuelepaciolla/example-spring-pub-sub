// PubSubMessage.java
package com.example.gcpdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PubSubMessage {
    private String id;
    private String data;
    private Map<String, String> attributes;
    private Long publishTime;
}