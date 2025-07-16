package com.lxy.ai.mapper;

import com.lxy.ai.entity.po.ChatHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatHistoryMapper {

    /**
     * 插入一条聊天记录
     * @param chatHistory
     */
    @Insert("INSERT INTO ai_chat.chat_history (type, chat_id,create_time) VALUES (#{type}, #{chatId}, #{createTime})")
    void insert(ChatHistory chatHistory);

    /**
     * 删除一条聊天记录
     * @param type
     * @param chatId
     */
    @Delete("DELETE FROM ai_chat.chat_history WHERE type = #{type} AND chat_id = #{chatId}")
    void delete(@Param("type") String type, @Param("chatId") String chatId);

    /**
     * 根据type获取聊天记录的chatIds
     * @param type
     * @return
     */
    @Select("SELECT chat_id FROM ai_chat.chat_history WHERE type = #{type} order by create_time")
    List<String> selectChatIdsByType(String type);
}
