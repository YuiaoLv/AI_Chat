package com.example.springaichat.config;

import com.example.springaichat.entity.po.InSqlChatMemory;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 聊天记忆配置类
 * 为不同功能配置不同的ChatMemory实现
 */
@Configuration
public class ChatMemoryConfig {

    /**
     * 默认的ChatMemory实现，使用数据库存储
     * 用于普通聊天和客服功能
     */
    @Bean
    @Primary
    public ChatMemory defaultChatMemory() {
        return new InSqlChatMemory();
    }

    /**
     * 游戏功能的ChatMemory实现，使用内存存储
     */
    @Bean
    public ChatMemory gameChatMemory() {
        return new InMemoryChatMemory();
    }

    /**
     * PDF功能的ChatMemory实现，使用内存存储
     */
    @Bean
    public ChatMemory pdfChatMemory() {
        return new InMemoryChatMemory();
    }
}