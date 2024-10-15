package com.ice.learningcompass.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ice.learningcompass.model.dto.course.CourseAddRequest;
import com.ice.learningcompass.model.dto.course.CourseEditRequest;
import com.ice.learningcompass.model.dto.course.CourseQueryRequest;
import com.ice.learningcompass.model.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.learningcompass.model.vo.CourseVO;

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

    /**
     * 编辑课程信息
     *
     * @param courseEditRequest 课程编辑请求体
     * @param teacherId         教师id
     * @return 编辑成功
     */
    Boolean editCourse(CourseEditRequest courseEditRequest, Long teacherId);

    /**
     * 解散课程
     *
     * @param courseId  课程 id
     * @param teacherId 教师 id
     * @return 解散成功
     */
    Boolean disbandCourse(Long courseId, Long teacherId);

    /**
     * 根据 Id 获取课程包装类
     *
     * @param id 课程 id
     * @return 课程包装类
     */
    CourseVO getCourseVO(long id);

    /**
     * 获取查询条件
     *
     * @param courseQueryRequest 课程查询请求
     * @return 查询条件
     */
    QueryWrapper<Course> getQueryWrapper(CourseQueryRequest courseQueryRequest);
}
