package com.ice.learningcompass.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.constant.UserConstant;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.mapper.CourseMapper;
import com.ice.learningcompass.mapper.CourseResourceMapper;
import com.ice.learningcompass.mapper.ResourceStudentMapper;
import com.ice.learningcompass.model.dto.courseresource.CourseResourceAddRequest;
import com.ice.learningcompass.model.entity.Course;
import com.ice.learningcompass.model.entity.CourseResource;
import com.ice.learningcompass.model.entity.ResourceStudent;
import com.ice.learningcompass.model.entity.User;
import com.ice.learningcompass.model.enums.CourseResourceTypeEnum;
import com.ice.learningcompass.model.vo.ResourceStudentVO;
import com.ice.learningcompass.service.CourseResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private ResourceStudentMapper resourceStudentMapper;

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

    @Override
    public Boolean deleteCourseResource(Long resourceId, User loginUser) {
        // 判断课程是否存在
        CourseResource courseResource = baseMapper.selectOne(Wrappers.<CourseResource>lambdaQuery()
                .eq(CourseResource::getId, resourceId)
                .select(CourseResource::getTeacherId)
                .last("limit 1"));
        ThrowUtils.throwIf(courseResource == null, ErrorCode.NOT_FOUND_ERROR, "Resource Not Fount!");

        // 如果当前用户是教师，判断课程是否为该教师创建
        String userRole = loginUser.getUserRole();
        if (UserConstant.TEACHER_ROLE.equals(userRole) &&
                !courseResource.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "Only Resources can be deleted from self-created resource!");
        }

        baseMapper.deleteById(resourceId);

        // todo 删除学生浏览记录

        return true;
    }

    @Override
    public List<ResourceStudentVO> getCourseResourceVO(Long courseId, User loginUser) {
        List<CourseResource> courseResourceList = baseMapper.selectList(
                Wrappers.<CourseResource>lambdaQuery()
                        .eq(CourseResource::getCourseId, courseId)
        );

        // 判断用户是否阅读过
        Set<Long> resourceIdSet = courseResourceList
                .stream().map(CourseResource::getId)
                .collect(Collectors.toSet());

        if (resourceIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        List<ResourceStudent> resourceStudentList = resourceStudentMapper.selectList(
                Wrappers.<ResourceStudent>lambdaQuery()
                        .in(ResourceStudent::getResourceId, resourceIdSet)
                        .eq(ResourceStudent::getStudentId, loginUser.getId())
                        .select(ResourceStudent::getViewTime, ResourceStudent::getViewNum, ResourceStudent::getResourceId)
        );
        Map<Long, ResourceStudent> resourceStudentMap = resourceStudentList.stream()
                .collect(Collectors.toMap(ResourceStudent::getResourceId, resourceStudent -> resourceStudent));

        return courseResourceList.stream()
                .map(resource -> {
                    ResourceStudentVO resourceStudentVO = ResourceStudentVO.objToVo(resource);
                    ResourceStudent resourceStudent = resourceStudentMap.get(resource.getId());
                    if (resourceStudent == null) {
                        resourceStudentVO.setIsRead(false);
                        return resourceStudentVO;
                    }
                    resourceStudentVO.setIsRead(true);
                    resourceStudentVO.setViewNum(resourceStudent.getViewNum());
                    resourceStudentVO.setViewTime(resourceStudent.getViewTime());
                    return resourceStudentVO;
                }).collect(Collectors.toList());
    }

    private void validCourseResource(CourseResource courseResource) {
        Long courseId = courseResource.getCourseId();
        String resourceName = courseResource.getResourceName();
        String resourceUrl = courseResource.getResourceUrl();
        Integer resourceType = courseResource.getResourceType();

        if (StringUtils.isAnyBlank(resourceName, resourceUrl)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Resource relevant information can not be empty!");
        }

        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Course Id can not be empty!");
        }

        if (resourceType == null || !CourseResourceTypeEnum.getValues().contains(resourceType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Resource Type Error!");
        }
    }
}




