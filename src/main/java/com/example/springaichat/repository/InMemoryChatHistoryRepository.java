package com.example.springaichat.repository;


import com.example.springaichat.entity.po.ChatHistory;
import com.example.springaichat.mapper.ChatHistoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service // 添加这个注解，表示这个类是一个组件，可以被Spring容器管理
public class InMemoryChatHistoryRepository implements ChatHistoryRepository{

      @Resource
    private ChatHistoryMapper chatHistoryMapper;

    /**
     * 保存会话记录到MySQL数据库
     * @param type 业务类型， 如：chat,service，pdf
     * @param chatId 会话id
     */
    @Override
    public void save(String type, String chatId) {
        // 创建ChatHistory对象
        if(chatHistoryMapper.getId(type,chatId)!=0) return;
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setType(type);
        chatHistory.setChatId(chatId);
        chatHistory.setCreateTime(LocalDateTime.now());
        
        // 调用Mapper插入数据
        chatHistoryMapper.insert(chatHistory);
    }

    /**
     * 从MySQL数据库获取会话ID列表
     * @param type 业务类型，如：chat、service、pdf
     * @return 会话ID列表
     */
    @Override
    public List<String> getChatIds(String type) {
        // 调用Mapper查询数据
        return chatHistoryMapper.getChatIdsByType(type);
    }

    @Override
    public void delete(String type, String chatId) {
        chatHistoryMapper.delete(type, chatId);
    }
}
