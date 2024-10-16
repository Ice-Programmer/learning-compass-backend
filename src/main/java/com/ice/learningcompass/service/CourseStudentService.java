package com.ice.learningcompass.service;

import com.ice.learningcompass.model.entity.CourseStudent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author chenjiahan
 * @description 针对表【course_student(课程)】的数据库操作Service
 * @createDate 2024-10-16 12:13:18
 */
public interface CourseStudentService extends IService<CourseStudent> {

    /**
     * 学生加入课程
     *
     * @param courseId  课程 id
     * @param studentId 学生 id
     * @return 加入记录 id
     */
    Long studentJoinCourse(Long courseId, Long studentId);
}
