package com.lxy.ai.repository;

import com.lxy.ai.entity.po.ChatHistory;
import com.lxy.ai.mapper.ChatHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public class InSqlChatHistoryRepository implements ChatHistoryRepository{
    @Autowired
    private ChatHistoryMapper chatHistoryMapper;

    @Override
    public void save(String type, String chatId) {
        if(exists(type,chatId)) return;
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setType(type);
        chatHistory.setChatId(chatId);
        chatHistory.setCreateTime(LocalDateTime.now());
        chatHistoryMapper.insert(chatHistory);
    }
    private boolean exists(String type, String chatId) {
        List<String> chatIds = chatHistoryMapper.selectChatIdsByType(type);
        return chatIds.contains(chatId);
    }

    @Override
    public List<String> getChatIds(String type) {
        return chatHistoryMapper.selectChatIdsByType(type);
    }

    @Override
    public void delete(String type, String chatId) {
        chatHistoryMapper.delete(type,chatId);
    }
}
