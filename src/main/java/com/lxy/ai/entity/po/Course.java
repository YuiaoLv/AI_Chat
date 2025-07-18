package com.lxy.ai.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学科表
 * </p>
 *
 * @author lxy
 * @since 2025-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学科名称
     */
    private String name;

    /**
     * 学历背景要求：0-无，1-大一，2-大二、3-大三及以上、4-研究生
     */
    private Integer edu;

    /**
     * 课程类型：编程、数学、哲学、通识、体育
     */
    private String type;

    /**
     * 学分
     */
    private Double credit;

    /**
     * 课程持续周数
     */
    private Integer durationWeeks;

    /**
     * 上课时间
     */
    private Integer classDay;


}
