package com.lxy.ai.tools;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.lxy.ai.entity.po.Course;
import com.lxy.ai.entity.po.CourseReservation;
import com.lxy.ai.entity.po.School;
import com.lxy.ai.entity.query.CourseQuery;
import com.lxy.ai.service.ICourseReservationService;
import com.lxy.ai.service.ICourseService;
import com.lxy.ai.service.ISchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseTools {
    private final ICourseService courseService;
    private final ISchoolService schoolService;
    private final ICourseReservationService courseReservationService;

    @Tool(description = "根据条件查询课程")
    public List<Course> queryCourse(@ToolParam(required = false, description = "课程查询条件") CourseQuery query) {
        if (query == null) {
            return courseService.list();
        }
        QueryChainWrapper<Course> wrapper = courseService.query();
        wrapper
                .like(query.getType() != null, "type", query.getType())  // 课程类型
                .le(query.getEdu() != null, "edu", query.getEdu())  // 学生年级要求
                .eq(query.getClassDay() != null, "class_day", query.getClassDay()); // 上课星期
        if (query.getCampusName() != null && !query.getCampusName().isEmpty()) {
            wrapper.exists(
                    "SELECT 1 FROM school s " +
                            "JOIN contains c ON s.id = c.school_id " +
                            "WHERE s.name LIKE CONCAT('%', {0}, '%') AND c.course_id = course.id",
                    query.getCampusName()
            );
        }
        if (query.getSorts() != null) {
            for (CourseQuery.Sort sort : query.getSorts()) {
                wrapper.orderBy(true, sort.getAsc(), sort.getField());
            }
        }
        return wrapper.list();
    }

    @Tool(description = "根据条件查询校区")
    public List<Course> queryCampus(@ToolParam(required = false, description = "校区名称") String name) {
        return courseService.query()
                .exists(
                        "SELECT 1 FROM school s " +
                                "JOIN contains c ON s.id = c.school_id " +
                                "WHERE s.name LIKE CONCAT('%', {0}, '%') AND c.course_id = course.id",
                        name
                ).list();
    }

    @Tool(description = "生成预约单，返回预约单号")
    public Integer getCourseReservation( @ToolParam(description = "预约课程") String course,
                                         @ToolParam(description = "学生姓名") String studentName,
                                         @ToolParam(description = "联系方式") String contactInfo,
                                         @ToolParam(description = "预约校区") String school,
                                         @ToolParam(required = false, description = "备注") String remark) {
        CourseReservation reservation = new CourseReservation();
        reservation.setCourse(course);
        reservation.setStudentName(studentName);
        reservation.setContactInfo(contactInfo);
        reservation.setSchool(school);
        reservation.setRemark(remark);
        courseReservationService.save(reservation);
        return reservation.getId();
    }

    @Tool(description = "查询符合用户年级的其它课程推荐")
    public List<Course> queryRecommendCourse(@ToolParam(description = "用户年级") Integer edu) {
        return courseService.query()
                .le("edu", edu)
                .orderByDesc("credit")
                .orderByAsc("duration_weeks")
                .list();
    }
    @Tool(description = "检测校区是否存在")
    public boolean checkSchoolExist(@ToolParam(description = "校区名称") String name) {
        return schoolService.query()
                .like("name", name)
                .count()>0;
    }
    @Tool(description = "查询所有校区信息")
    public List<School> queryAllSchool() {
        return schoolService.list();
    }
}
