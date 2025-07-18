package com.lxy.ai.entity.query;

import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;

@Data
public class CourseQuery {
    // @ToolParam：描述参数，是SpringAI提供的用来解释Function参数的注解。其中的信息都会通过提示词的方式发送给AI模型。
    @ToolParam(required = false, description = "课程类型：编程、数学、哲学、通识、体育")
    private String type;
    @ToolParam(required = false, description = "学生年级要求：1-至少大一，2-至少大二，3-至少大三，4-至少大四")
    private Integer edu;
    @ToolParam(required = false, description = "课程学分：可取值1、1.5、2、2.5、3、3.5、4")
    private double credit;
    @ToolParam(required = false, description = "学习时长，单位: 周")
    private Integer durationWeeks;
    @ToolParam(required = false, description = "上课星期：如星期一到星期天")
    private String classDay;
    @ToolParam(required = false, description = "校区名称")
    private String campusName;
    @ToolParam(required = false, description = "排序方式")
    private List<Sort> sorts;


    @Data
    public static class Sort {
        @ToolParam(required = false, description = "排序字段: credit或durationWeeks")
        private String field;
        @ToolParam(required = false, description = "是否是升序: true/false")
        private Boolean asc;
    }
}
