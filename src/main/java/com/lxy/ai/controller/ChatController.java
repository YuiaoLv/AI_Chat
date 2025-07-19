package com.lxy.ai.controller;

import com.lxy.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    @Autowired
    @Qualifier("inSqlChatHistoryRepository")
    private ChatHistoryRepository chatHistoryRepository;

    @RequestMapping(value = "/chat",produces = "text/html;charset=utf-8")
    public Flux<String> chat(String prompt,String chatId,@RequestParam(value = "files", required = false) List<MultipartFile> files) {
        chatHistoryRepository.save("chat",chatId);

        if (files != null && !files.isEmpty()) {
            // 有附件，多模态聊天
            return multiModalChat(prompt, chatId, files);
        } else {
            // 没有附件，纯文本聊天
            return textChat(prompt, chatId);
        }
    }

    private Flux<String> multiModalChat(String prompt, String chatId, List<MultipartFile> files) {
        // 1.解析多媒体文件
        List<Media> medias = files.stream()
                .map(file -> new Media(
                                MimeType.valueOf(Objects.requireNonNull(file.getContentType())),
                                file.getResource()
                        )
                )
                .toList();
        // 2.请求模型
        return chatClient.prompt()
                .user(p -> p.text(prompt).media(medias.toArray(Media[]::new)))// 设置用户输入
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,chatId))// 设置会话ID
                .stream()// 开启流式对话
                .content();// 获取对话内容
    }

    private Flux<String> textChat(String prompt, String chatId) {
        return chatClient.prompt()
                .user(prompt)// 设置用户输入
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,chatId))// 设置会话ID
                .stream()// 开启流式对话
                .content();// 获取对话内容
    }

}
