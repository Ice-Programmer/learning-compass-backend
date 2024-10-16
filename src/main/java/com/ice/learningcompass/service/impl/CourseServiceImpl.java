package com.ice.learningcompass.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.constant.CommonConstant;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.mapper.CourseMapper;
import com.ice.learningcompass.mapper.CourseStudentMapper;
import com.ice.learningcompass.mapper.UserMapper;
import com.ice.learningcompass.model.dto.course.CourseAddRequest;
import com.ice.learningcompass.model.dto.course.CourseEditRequest;
import com.ice.learningcompass.model.dto.course.CourseQueryRequest;
import com.ice.learningcompass.model.dto.course.CourseUpdateRequest;
import com.ice.learningcompass.model.entity.Course;
import com.ice.learningcompass.model.entity.CourseStudent;
import com.ice.learningcompass.model.entity.User;
import com.ice.learningcompass.model.enums.StatusTypeEnum;
import com.ice.learningcompass.model.vo.CourseVO;
import com.ice.learningcompass.model.vo.UserVO;
import com.ice.learningcompass.service.CourseService;
import com.ice.learningcompass.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chenjiahan
 * @description 针对表【course(课程)】的数据库操作Service实现
 * @createDate 2024-10-15 13:19:27
 */
@Service
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
        implements CourseService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CourseStudentMapper courseStudentMapper;

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

    @Override
    public Boolean editCourse(CourseEditRequest courseEditRequest, Long teacherId) {
        // 判断课程是否存在
        boolean exists = baseMapper.exists(Wrappers.<Course>lambdaQuery()
                .eq(Course::getId, courseEditRequest.getId())
                .eq(Course::getTeacherId, teacherId));
        ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "Course not exists！");

        // 校验参数
        Course course = new Course();
        BeanUtils.copyProperties(courseEditRequest, course);
        validCourse(course, false);
        List<String> tagList = courseEditRequest.getTagList();
        if (!CollectionUtils.isEmpty(tagList)) {
            String tags = GSON.toJson(tagList);
            course.setTags(tags);
        }

        // 更新数据库
        baseMapper.updateById(course);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean disbandCourse(Long courseId, Long teacherId) {
        // 判断课程是否存在
        boolean exists = baseMapper.exists(Wrappers.<Course>lambdaQuery()
                .eq(Course::getId, courseId)
                .eq(Course::getTeacherId, teacherId));
        ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "Course not exists！");

        // 删除课程
        baseMapper.deleteById(courseId);
        log.info("Successfully deleted course with id {}", courseId);

        // 删除上课学生
        int deleteCourseStudentRows = courseStudentMapper.delete(Wrappers.<CourseStudent>lambdaQuery()
                .eq(CourseStudent::getCourseId, courseId));
        log.info("Successfully deleted {} student-course records for course {}", deleteCourseStudentRows, courseId);

        // todo 删除课程资料

        return true;
    }

    @Override
    public CourseVO getCourseVO(long id) {
        // 查询课程信息
        Course course = baseMapper.selectById(id);
        ThrowUtils.throwIf(course == null, ErrorCode.NOT_FOUND_ERROR, "Course not exists");
        CourseVO courseVO = CourseVO.objToVo(course);

        // 获取教师信息
        Long teacherId = course.getTeacherId();
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId, teacherId));
        UserVO teacherInfo = UserVO.objToVo(user);
        courseVO.setTeacherInfo(teacherInfo);

        return courseVO;
    }

    @Override
    public QueryWrapper<Course> getQueryWrapper(CourseQueryRequest courseQueryRequest) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (courseQueryRequest == null) {
            return queryWrapper;
        }
        String searchText = courseQueryRequest.getSearchText();
        String sortField = courseQueryRequest.getSortField();
        String sortOrder = courseQueryRequest.getSortOrder();
        Long id = courseQueryRequest.getId();
        String name = courseQueryRequest.getName();
        String description = courseQueryRequest.getDescription();
        List<String> tagList = courseQueryRequest.getTags();
        Long teacherId = courseQueryRequest.getTeacherId();
        Long notId = courseQueryRequest.getNotId();
        // 拼接查询条件
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
        }
        queryWrapper.like(StringUtils.isNotBlank(name), "title", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(teacherId), "teacherId", teacherId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<CourseVO> pageCourseVO(CourseQueryRequest courseQueryRequest) {
        long current = courseQueryRequest.getCurrent();
        long size = courseQueryRequest.getPageSize();

        // 获取课程分页
        Page<Course> coursePage = baseMapper.selectPage(new Page<>(current, size),
                getQueryWrapper(courseQueryRequest));

        List<Course> courseList = coursePage.getRecords();
        // 获取所有创建教师 id
        Set<Long> teacherIds = courseList.stream()
                .map(Course::getTeacherId)
                .collect(Collectors.toSet());

        // 获取所有教师 信息
        List<User> teacherList = userMapper.selectList(Wrappers.<User>lambdaQuery()
                .in(User::getId, teacherIds));
        // 获取教师信息 map 【teacherId -> teacherInfo】
        Map<Long, UserVO> teacherInfoMap = teacherList.stream()
                .map(UserVO::objToVo)
                .collect(Collectors.toMap(UserVO::getId, user -> user));

        // 封装包装类
        List<CourseVO> courseVOList = courseList.stream().map(course -> {
            CourseVO courseVO = CourseVO.objToVo(course);
            courseVO.setTeacherInfo(teacherInfoMap.get(course.getTeacherId()));
            return courseVO;
        }).collect(Collectors.toList());

        Page<CourseVO> courseVOPage = new Page<>(coursePage.getCurrent(), coursePage.getSize(), coursePage.getTotal());

        courseVOPage.setRecords(courseVOList);

        return courseVOPage;
    }

    @Override
    public Boolean updateCourse(CourseUpdateRequest courseUpdateRequest) {
        // 判断课程是否存在
        boolean exists = baseMapper.exists(Wrappers.<Course>lambdaQuery()
                .eq(Course::getId, courseUpdateRequest.getId()));
        ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "Course not exists！");

        // 校验参数
        Course course = new Course();
        BeanUtils.copyProperties(courseUpdateRequest, course);
        validCourse(course, false);
        List<String> tagList = courseUpdateRequest.getTagList();
        if (!CollectionUtils.isEmpty(tagList)) {
            String tags = GSON.toJson(tagList);
            course.setTags(tags);
        }


        // 更新数据库
        baseMapper.updateById(course);

        return true;
    }

    private void validCourse(Course course, boolean add) {
        if (course == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Course cannot be null");
        }
        String name = course.getName();
        String description = course.getDescription();
        String tags = course.getTags();
        Long teacherId = course.getTeacherId();
        Integer status = course.getStatus();
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
        if (status != null && !StatusTypeEnum.getValues().contains(status)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Status is only 0 or 1");
        }
    }
}




