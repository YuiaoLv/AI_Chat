package com.example.springaichat.entity.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatHistory {
    private String chatId;
    private String type;
    private LocalDateTime createTime;
}
