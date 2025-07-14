package com.example.springaichat.config;

import com.example.springaichat.repository.ChatHistoryRepository;
import com.example.springaichat.repository.InMemoryChatHistoryRepository;
import com.example.springaichat.repository.InSqlChatHistoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 聊天历史记录仓库配置类
 * 为不同功能配置不同的ChatHistoryRepository实现
 */
@Configuration
public class ChatHistoryRepositoryConfig {

    /**
     * 默认的ChatHistoryRepository实现，使用数据库存储
     * 用于普通聊天和客服功能
     */
    @Bean
    @Primary
    public ChatHistoryRepository defaultChatHistoryRepository() {
        return new InSqlChatHistoryRepository();
    }

    /**
     * 游戏功能的ChatHistoryRepository实现，使用内存存储
     */
    @Bean
    public ChatHistoryRepository gameChatHistoryRepository() {
        return new InMemoryChatHistoryRepository();
    }

    /**
     * PDF功能的ChatHistoryRepository实现，使用内存存储
     */
    @Bean
    public ChatHistoryRepository pdfChatHistoryRepository() {
        return new InMemoryChatHistoryRepository();
    }
}