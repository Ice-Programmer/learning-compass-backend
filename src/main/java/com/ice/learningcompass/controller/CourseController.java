package com.ice.learningcompass.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.learningcompass.common.BaseResponse;
import com.ice.learningcompass.common.DeleteRequest;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.common.ResultUtils;
import com.ice.learningcompass.constant.UserConstant;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.model.dto.course.CourseAddRequest;
import com.ice.learningcompass.model.dto.course.CourseEditRequest;
import com.ice.learningcompass.model.dto.course.CourseQueryRequest;
import com.ice.learningcompass.model.entity.Course;
import com.ice.learningcompass.model.entity.User;
import com.ice.learningcompass.model.vo.CourseVO;
import com.ice.learningcompass.service.CourseService;
import com.ice.learningcompass.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 课程相关接口
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/15 13:21
 */
@Slf4j
@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    @Resource
    private UserService userService;

    /**
     * 创建课程
     *
     * @param courseAddRequest 课程新增请求体
     * @return 课程id
     */
    @PostMapping("/add")
    @SaCheckRole(UserConstant.TEACHER_ROLE)
    public BaseResponse<Long> addCourse(@RequestBody CourseAddRequest courseAddRequest) {
        if (courseAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser();
        Long courseId = courseService.addCourse(courseAddRequest, loginUser.getId());

        return ResultUtils.success(courseId);
    }


    /**
     * 编辑课程信息
     *
     * @param courseEditRequest 课程编辑请求体
     * @return 编辑成功
     */
    @PostMapping("/edit")
    @SaCheckRole(value = {UserConstant.TEACHER_ROLE})
    public BaseResponse<Boolean> editCourse(@RequestBody CourseEditRequest courseEditRequest) {
        ThrowUtils.throwIf(courseEditRequest == null, ErrorCode.PARAMS_ERROR);
        if (courseEditRequest.getId() == null || courseEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Course Id error!");
        }
        // 获取当前登陆用户
        User loginUser = userService.getLoginUser();

        Boolean result = courseService.editCourse(courseEditRequest, loginUser.getId());

        return ResultUtils.success(result);
    }

    /**
     * 教师解散课程
     *
     * @param deleteRequest 删除请求
     * @return 删除成功
     */
    @PostMapping("/delete")
    @SaCheckRole(value = {UserConstant.TEACHER_ROLE})
    public BaseResponse<Boolean> disbandCourse(DeleteRequest deleteRequest) {
        if (deleteRequest.getId() == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取当前登录用户
        User loginUser = userService.getLoginUser();

        Boolean result = courseService.disbandCourse(deleteRequest.getId(), loginUser.getId());

        return ResultUtils.success(result);
    }

    /**
     * 获取课程包装类信息
     *
     * @param id 课程 id
     * @return 课程包装类
     */
    @GetMapping("/get/vo")
    public BaseResponse<CourseVO> getCourseVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        CourseVO courseVO = courseService.getCourseVO(id);

        return ResultUtils.success(courseVO);
    }

    /**
     * 分页获取课程列表（仅管理员）
     *
     * @param courseQueryRequest 课程查询请求
     * @return 课程分页
     */
    @PostMapping("/list/page")
    @SaCheckRole(value = {UserConstant.ADMIN_ROLE, UserConstant.SUPER_ADMIN_ROLE}, mode = SaMode.OR)
    public BaseResponse<Page<Course>> pageCourse(@RequestBody CourseQueryRequest courseQueryRequest) {
        long current = courseQueryRequest.getCurrent();
        long size = courseQueryRequest.getPageSize();
        Page<Course> coursePage = courseService.page(new Page<>(current, size),
                courseService.getQueryWrapper(courseQueryRequest));
        return ResultUtils.success(coursePage);
    }

    /**
     * 分页获取课程封装类
     *
     * @param courseQueryRequest 课程查询请求
     * @return 课程封装类分页
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CourseVO>> pageCourseVO(@RequestBody CourseQueryRequest courseQueryRequest) {
        Page<CourseVO> courseVOPage = courseService.pageCourseVO(courseQueryRequest);

        return ResultUtils.success(courseVOPage);
    }
}
