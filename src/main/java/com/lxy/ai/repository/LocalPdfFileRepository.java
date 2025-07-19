package com.lxy.ai.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.ai.entity.po.ChatFileMapping;
import com.lxy.ai.mapper.ChatFileMappingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LocalPdfFileRepository implements FileRepository {

    private final ChatFileMappingMapper chatFileMappingMapper;

    @Override
    public boolean save(String chatId, Resource resource) {
        String filename = resource.getFilename();
        File target = new File(Objects.requireNonNull(filename));
        if (!target.exists()) {
            try {
                Files.copy(resource.getInputStream(), target.toPath());
            } catch (IOException e) {
                log.error("Failed to save PDF resource.", e);
                return false;
            }
        }
        // 保存映射关系到数据库
        ChatFileMapping mapping = new ChatFileMapping();
        mapping.setChatId(chatId);
        mapping.setFileName(filename);
        chatFileMappingMapper.insert(mapping); // 插入数据库
        return true;
    }

    @Override
    public Resource getFile(String chatId) {
        // 使用 LambdaQueryWrapper 通过 chatId 字段查询
        LambdaQueryWrapper<ChatFileMapping> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatFileMapping::getChatId, chatId);

        // 查询对应的映射记录
        ChatFileMapping mapping = chatFileMappingMapper.selectOne(queryWrapper);

        if (mapping != null) {
            return new FileSystemResource(mapping.getFileName());
        }
        return null;
    }
}
