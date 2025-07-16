package com.example.springaichat.controller;

import com.example.springaichat.entity.po.InSqlChatMemory;
import com.example.springaichat.entity.vo.MessageVO;
import com.example.springaichat.repository.ChatHistoryRepository;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.messages.Message;
import java.util.List;

@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {

    @Autowired
    @Qualifier("inSqlChatHistoryRepository")
    private  ChatHistoryRepository chatHistoryRepository;

    @Resource
    private InSqlChatMemory inSqlChatMemory;

    /**
     * 查询会话历史列表
     * @param type 业务类型，如：chat,service,pdf
     * @return chatId列表
     */
    @GetMapping("/{type}")
    public List<String> getChatIds(@PathVariable("type") String type) {
        return chatHistoryRepository.getChatIds(type);
    }

    /**
     * 根据业务类型、chatId查询会话历史
     * @param type 业务类型，如：chat,service,pdf
     * @param chatId 会话id
     * @return 指定会话的历史消息
     */
    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> getChatHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        List<Message> messages = inSqlChatMemory.get(chatId, Integer.MAX_VALUE); // 获取指定会话的所有消息
        if(messages == null) {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();  // 将消息转换为VO
    }

    @DeleteMapping("/{type}/{chatId}")
    public void deleteChatHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        chatHistoryRepository.delete(type, chatId);
        inSqlChatMemory.clear(chatId);
    }
}