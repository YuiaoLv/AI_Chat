package com.lxy.ai.entity.po;

import lombok.Data;

@Data
public class ChatMessage {
    private Long id;
    private String conversationId;
    private String role; // "USER", "ASSISTANT"
    private String content;
}
