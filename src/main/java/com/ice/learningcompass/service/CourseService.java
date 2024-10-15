package com.ice.learningcompass.service;

import com.ice.learningcompass.model.dto.course.CourseAddRequest;
import com.ice.learningcompass.model.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author chenjiahan
 * @description 针对表【course(课程)】的数据库操作Service
 * @createDate 2024-10-15 13:19:27
 */
public interface CourseService extends IService<Course> {

    /**
     * 创建课程
     *
     * @param courseAddRequest 课程新增请求体
     * @param teacherId        教师id
     * @return 课程id
     */
    Long addCourse(CourseAddRequest courseAddRequest, Long teacherId);
}
