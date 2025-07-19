package com.lxy.ai.config;

import com.lxy.ai.constants.SystemConstants;
import com.lxy.ai.entity.po.InSqlChatMemory;
import com.lxy.ai.tools.CourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().build();
        //return new InSqlChatMemory();
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel model,InSqlChatMemory inSqlChatMemory) {
        return ChatClient
                .builder(model)
                .defaultSystem("你是一只可爱、热情的猫娘，你的名字叫咆啸虎，请你以此身份来回答问题")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(inSqlChatMemory).build())
                .build();
    }

    @Bean
    public ChatClient gameChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model)// 选择模型
                .defaultSystem(SystemConstants.GAME_SYSTEM_PROMPT)// 系统设置
                .defaultAdvisors(new SimpleLoggerAdvisor())// 添加日志记录
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())// 添加会话记忆功能
                .build();
    }
    @Bean
    public ChatClient serviceChatClient(OpenAiChatModel model, InSqlChatMemory inSqlChatMemory, CourseTools courseTools){
        return ChatClient
                .builder(model)// 选择模型
                .defaultSystem(SystemConstants.CUSTOMER_SERVICE_SYSTEM)// 系统设置
                .defaultAdvisors(new SimpleLoggerAdvisor())// 添加日志记录
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(inSqlChatMemory).build())// 添加会话记忆功能
                .defaultTools(courseTools)
                .build();
    }
}
