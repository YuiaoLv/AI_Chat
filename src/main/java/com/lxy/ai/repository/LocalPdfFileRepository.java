package com.lxy.ai.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.ai.entity.po.ChatFileMapping;
import com.lxy.ai.mapper.ChatFileMappingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LocalPdfFileRepository implements FileRepository {

    private final ChatFileMappingMapper chatFileMappingMapper;

    private final VectorStore vectorStore;

    @Override
    public boolean save(String chatId, Resource resource) {
        // 保存到本地磁盘
        String filename = resource.getFilename();
        File target = new File(Objects.requireNonNull(filename));
        if (!target.exists()) {
            try {
                Files.copy(resource.getInputStream(), target.toPath());
            } catch (IOException e) {
                log.error("Failed to save PDF resource.", e);
                return false;
            }
            // 如果文件是第一次保存，则保存到向量库
            writeToVectorStore(resource);
        }

        // 使用 LambdaQueryWrapper 通过实体类属性名查询
        LambdaQueryWrapper<ChatFileMapping> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatFileMapping::getChatId, chatId);

        // 查询是否存在记录
        ChatFileMapping existingMapping = chatFileMappingMapper.selectOne(queryWrapper);

        // 如果不存在，保存新的关系
        if (existingMapping == null) {
            ChatFileMapping mapping = new ChatFileMapping();
            mapping.setChatId(chatId);
            mapping.setFileName(filename);
            chatFileMappingMapper.insert(mapping); // 插入数据库
        }

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

    @Override
    public void delete(String chatId) {
        // 使用 LambdaQueryWrapper 通过 chatId 字段查询
        LambdaQueryWrapper<ChatFileMapping> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatFileMapping::getChatId, chatId);

        // 删除对应的映射记录
        chatFileMappingMapper.delete(queryWrapper);
    }

    private void writeToVectorStore(Resource resource) {
        // 1.创建PDF的读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );
        // 2.读取PDF文档，拆分为Document
        List<Document> documents = reader.read();
        // 3.写入向量库
        vectorStore.add(documents);
    }
}
