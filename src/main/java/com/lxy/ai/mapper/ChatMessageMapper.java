package com.lxy.ai.mapper;


import com.lxy.ai.entity.po.ChatMessage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMessageMapper {
    @Insert("INSERT INTO ai_chat.chat_message (conversation_id, role, content) VALUES (#{conversationId}, #{role}, #{content})")
    void save(ChatMessage message);
    @Select("SELECT * FROM ai_chat.chat_message WHERE conversation_id = #{conversationId} ORDER BY id")
    List<ChatMessage> findByConversationId(String conversationId);
    @Delete("DELETE FROM ai_chat.chat_message WHERE conversation_id = #{conversationId}")
    void deleteByConversationId(String conversationId);
}
