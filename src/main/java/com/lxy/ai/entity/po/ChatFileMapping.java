package com.lxy.ai.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lxy
 * @since 2025-07-19
 */
@TableName("chat_file_mapping")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChatFileMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String chatId;

    private String fileName;


    @Override
    public String toString() {
        return "ChatFileMapping{" +
            "id=" + id +
            ", chatId=" + chatId +
            ", fileName=" + fileName +
        "}";
    }
}
