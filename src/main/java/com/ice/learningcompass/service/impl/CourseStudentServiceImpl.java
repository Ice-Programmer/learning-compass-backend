package com.ice.learningcompass.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.mapper.CourseMapper;
import com.ice.learningcompass.mapper.CourseStudentMapper;
import com.ice.learningcompass.model.entity.Course;
import com.ice.learningcompass.model.entity.CourseStudent;
import com.ice.learningcompass.model.enums.StatusTypeEnum;
import com.ice.learningcompass.service.CourseStudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author chenjiahan
 * @description 针对表【course_student(课程)】的数据库操作Service实现
 * @createDate 2024-10-16 12:13:18
 */
@Service
public class CourseStudentServiceImpl extends ServiceImpl<CourseStudentMapper, CourseStudent>
        implements CourseStudentService {

    @Resource
    private CourseMapper courseMapper;

    @Override
    public Long studentJoinCourse(Long courseId, Long studentId) {
        // 判断课程是否存在
        Course course = courseMapper.selectOne(
                Wrappers.<Course>lambdaQuery()
                        .eq(Course::getId, courseId)
                        .select(Course::getStatus, Course::getEndTime)
                        .last("limit 1")
        );

        // 判断课程是否能加入
        // 1. 判断课程是否已经被关闭
        if (StatusTypeEnum.CLOSE.getValue().equals(course.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "Course has been closed!");
        }
        // 2. 判断课程是否已经结束
        Date currentTime = new Date();
        Date endTime = course.getEndTime();
        if (currentTime.after(endTime)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "Current time is after course end time!");
        }

        // 判断是否已经加入过课程
        boolean exists = baseMapper.exists(Wrappers.<CourseStudent>lambdaQuery()
                .eq(CourseStudent::getCourseId, courseId)
                .eq(CourseStudent::getStudentId, studentId));
        ThrowUtils.throwIf(exists, ErrorCode.OPERATION_ERROR, "You have already joined the course!");

        // 插入加入记录
        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setCourseId(courseId);
        courseStudent.setStudentId(studentId);
        baseMapper.insert(courseStudent);

        return courseStudent.getId();
    }
}




