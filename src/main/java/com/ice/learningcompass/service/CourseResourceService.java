package com.ice.learningcompass.service;

import com.ice.learningcompass.model.dto.courseresource.CourseResourceAddRequest;
import com.ice.learningcompass.model.entity.CourseResource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.learningcompass.model.entity.User;

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
     * @return 课程资料 id
     */
    Long addCourseResource(CourseResourceAddRequest courseResourceAddRequest, Long teacherId);

    /**
     * 删除课程资料
     *
     * @param resourceId 资料 id
     * @param loginUser  当前登录用户
     * @return
     */
    Boolean deleteCourseResource(Long resourceId, User loginUser);
}
