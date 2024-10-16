package com.ice.learningcompass.service;

import com.ice.learningcompass.model.dto.courseresource.CourseResourceAddRequest;
import com.ice.learningcompass.model.entity.CourseResource;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author chenjiahan
 * @description 针对表【course_resource(课程资料)】的数据库操作Service
 * @createDate 2024-10-16 15:18:14
 */
public interface CourseResourceService extends IService<CourseResource> {

    /**
     * 教师添加课程资料
     *
     * @param courseResourceAddRequest 添加课程资料请求
     * @param teacherId                教师 id
     * @return
     */
    Long addCourseResource(CourseResourceAddRequest courseResourceAddRequest, Long teacherId);
}
