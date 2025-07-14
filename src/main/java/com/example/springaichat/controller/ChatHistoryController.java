package com.example.springaichat.controller;

import com.example.springaichat.entity.vo.MessageVO;
import com.example.springaichat.repository.ChatHistoryRepository;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.messages.Message;
import java.util.List;

@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {

    @Resource
    @Qualifier("defaultChatHistoryRepository")
    private ChatHistoryRepository defaultChatHistoryRepository;
    
    @Resource
    @Qualifier("gameChatHistoryRepository")
    private ChatHistoryRepository gameChatHistoryRepository;
    
    @Resource
    @Qualifier("pdfChatHistoryRepository")
    private ChatHistoryRepository pdfChatHistoryRepository;
    
    @Resource
    @Qualifier("defaultChatMemory")
    private ChatMemory defaultChatMemory;
    
    @Resource
    @Qualifier("gameChatMemory")
    private ChatMemory gameChatMemory;
    
    @Resource
    @Qualifier("pdfChatMemory")
    private ChatMemory pdfChatMemory;
    
    // 根据类型获取对应的ChatHistoryRepository
    private ChatHistoryRepository getChatHistoryRepositoryByType(String type) {
        switch (type) {
            case "game":
                return gameChatHistoryRepository;
            case "pdf":
                return pdfChatHistoryRepository;
            default:
                return defaultChatHistoryRepository;
        }
    }
    
    // 根据类型获取对应的ChatMemory
    private ChatMemory getChatMemoryByType(String type) {
        switch (type) {
            case "game":
                return gameChatMemory;
            case "pdf":
                return pdfChatMemory;
            default:
                return defaultChatMemory;
        }
    }

    /**
     * 查询会话历史列表
     * @param type 业务类型，如：chat,service,game,pdf
     * @return chatId列表
     */
    @GetMapping("/{type}")
    public List<String> getChatIds(@PathVariable("type") String type) {
        return getChatHistoryRepositoryByType(type).getChatIds(type);
    }

    /**
     * 根据业务类型、chatId查询会话历史
     * @param type 业务类型，如：chat,service,game,pdf
     * @param chatId 会话id
     * @return 指定会话的历史消息
     */
    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> getChatHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        ChatMemory memory = getChatMemoryByType(type);
        List<Message> messages = memory.get(chatId, Integer.MAX_VALUE); // 获取指定会话的所有消息
        if(messages == null) {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();  // 将消息转换为VO
    }

    /**
     * 删除指定类型和chatId的会话历史
     * @param type 业务类型，如：chat,service,game,pdf
     * @param chatId 会话id
     */
    @DeleteMapping("/{type}/{chatId}")
    public void deleteChatHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        getChatHistoryRepositoryByType(type).delete(type, chatId);
        getChatMemoryByType(type).clear(chatId);
    }
}
