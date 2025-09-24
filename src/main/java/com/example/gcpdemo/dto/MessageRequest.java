// MessageRequest.java
package com.example.gcpdemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    
    @NotBlank(message = "Message text cannot be blank")
    @Size(max = 1000, message = "Message text cannot exceed 1000 characters")
    private String text;
    
    private String source = "API";
}