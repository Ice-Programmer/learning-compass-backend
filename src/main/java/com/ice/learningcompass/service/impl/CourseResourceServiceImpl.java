package com.ice.learningcompass.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.mapper.CourseMapper;
import com.ice.learningcompass.model.dto.courseresource.CourseResourceAddRequest;
import com.ice.learningcompass.model.entity.Course;
import com.ice.learningcompass.model.entity.CourseResource;
import com.ice.learningcompass.service.CourseResourceService;
import com.ice.learningcompass.mapper.CourseResourceMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @author chenjiahan
 * @description 针对表【course_resource(课程资料)】的数据库操作Service实现
 * @createDate 2024-10-16 15:18:14
 */
@Service
public class CourseResourceServiceImpl extends ServiceImpl<CourseResourceMapper, CourseResource>
        implements CourseResourceService {

    @Resource
    private CourseMapper courseMapper;

    @Override
    public Long addCourseResource(CourseResourceAddRequest courseResourceAddRequest, Long teacherId) {
        // 校验参数
        CourseResource courseResource = new CourseResource();
        BeanUtils.copyProperties(courseResourceAddRequest, courseResource);
        validCourseResource(courseResource);

        // 判断课程是否为该教师创建
        Course course = courseMapper.selectOne(
                Wrappers.<Course>lambdaQuery()
                        .eq(Course::getId, courseResourceAddRequest.getCourseId())
                        .select(Course::getTeacherId)
                        .last("limit 1")
        );
        ThrowUtils.throwIf(course == null, ErrorCode.NOT_FOUND_ERROR, "Course Not Fount!");
        if (!Objects.equals(course.getTeacherId(), teacherId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "Only Resources can be added to self-created courses!");
        }

        // 插入数据库
        courseResource.setTeacherId(teacherId);
        baseMapper.insert(courseResource);

        return courseResource.getId();
    }

    private void validCourseResource(CourseResource courseResource) {
        Long courseId = courseResource.getCourseId();
        String resourceName = courseResource.getResourceName();
        String resourceUrl = courseResource.getResourceUrl();

        if (StringUtils.isAnyBlank(resourceName, resourceUrl)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Resource relevant information can not be empty!");
        }

        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Course Id can not be empty!");
        }
    }
}




