package com.ice.learningcompass.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.model.dto.course.CourseAddRequest;
import com.ice.learningcompass.model.entity.Course;
import com.ice.learningcompass.service.CourseService;
import com.ice.learningcompass.mapper.CourseMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author chenjiahan
 * @description 针对表【course(课程)】的数据库操作Service实现
 * @createDate 2024-10-15 13:19:27
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
        implements CourseService {

    private final static Gson GSON = new Gson();

    @Override
    public Long addCourse(CourseAddRequest courseAddRequest, Long teacherId) {
        // 1. 校验参数
        Course course = new Course();
        BeanUtils.copyProperties(courseAddRequest, course);
        List<String> tagList = courseAddRequest.getTagList();
        if (!CollectionUtils.isEmpty(tagList)) {
            String tags = GSON.toJson(tagList);
            course.setTags(tags);
        }
        course.setTeacherId(teacherId);
        validCourse(course, true);
        // 插入数据库
        boolean result = baseMapper.insert(course) != 0;
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return course.getId();
    }

    private void validCourse(Course course, boolean add) {
        if (course == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Course cannot be null");
        }
        String name = course.getName();
        String description = course.getDescription();
        String tags = course.getTags();
        Long teacherId = course.getTeacherId();
        Date startTime = course.getStartTime();
        Date endTime = course.getEndTime();

        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, description, tags), ErrorCode.PARAMS_ERROR, "Name, description, and tags cannot be blank");
            if (teacherId == null || teacherId <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Teacher id cannot be null");
            }
            if (startTime == null || endTime == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Start time and end time cannot be null");
            }
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Title is too long");
        }
        if (StringUtils.isNotBlank(description) && description.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Description is too long");
        }
        if (startTime != null && endTime != null && startTime.after(endTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Start time cannot be after end time");
        }
    }
}




