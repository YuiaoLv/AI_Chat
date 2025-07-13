package com.example.springaichat.entity.po;

import lombok.Data;

@Data
public class ChatMessage {
    private String conversationId;
    private String role; // "USER", "ASSISTANT"
    private String content;
}
