package com.example.springaichat.mapper;

import com.example.springaichat.entity.po.ChatHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatHistoryMapper {

    @Insert("insert into ai_chat.chat_history(chat_id, type, create_time) values(#{chatId}, #{type}, #{createTime})")
    void insert(ChatHistory chatHistory);

    @Delete("delete from ai_chat.chat_history where type = #{type} and chat_id = #{chatId}")
    void delete(@Param("type") String type, @Param("chatId") String chatId);

    @Select("select chat_id from ai_chat.chat_history where type = #{type} order by create_time desc")
    List<String> getChatIdsByType(String type);


    @Select("select count(*) from ai_chat.chat_history where type = #{type} and chat_id = #{chatId}")
    int getId(String type, String chatId);
}
