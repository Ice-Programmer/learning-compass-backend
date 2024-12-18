package com.ice.learningcompass.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.ice.learningcompass.common.BaseResponse;
import com.ice.learningcompass.common.DeleteRequest;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.common.ResultUtils;
import com.ice.learningcompass.constant.UserConstant;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.model.dto.courseresource.CourseResourceAddRequest;
import com.ice.learningcompass.model.entity.User;
import com.ice.learningcompass.model.vo.ResourceStudentVO;
import com.ice.learningcompass.service.CourseResourceService;
import com.ice.learningcompass.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/16 13:54
 */
@RestController
@RequestMapping("/course/resource")
public class CourseResourceController {

    @Resource
    private CourseResourceService courseResourceService;

    @Resource
    private UserService userService;

    /**
     * 教师添加资料
     *
     * @param courseResourceAddRequest 添加课程资料请求
     * @return 课程资料 id
     */
    @PostMapping("/add")
    @SaCheckRole(UserConstant.TEACHER_ROLE)
    public BaseResponse<Long> addCourseResource(@RequestBody CourseResourceAddRequest courseResourceAddRequest) {
        ThrowUtils.throwIf(courseResourceAddRequest == null, ErrorCode.PARAMS_ERROR);

        // 获取当前登录用户
        User loginUser = userService.getLoginUser();

        Long resourceId = courseResourceService.addCourseResource(courseResourceAddRequest, loginUser.getId());

        return ResultUtils.success(resourceId);
    }

    /**
     * 删除课程资料
     *
     * @param deleteRequest 删除请求
     * @return 删除成功
     */
    @PostMapping("/delete")
    @SaCheckRole(value = {UserConstant.TEACHER_ROLE, UserConstant.ADMIN_ROLE, UserConstant.SUPER_ADMIN_ROLE}, mode = SaMode.OR)
    public BaseResponse<Boolean> deleteCourse(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        Long resourceId = deleteRequest.getId();
        if (resourceId == null || resourceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Resource id can not be empty!");
        }

        // 获取当前登录用户
        User loginUser = userService.getLoginUser();

        Boolean result = courseResourceService.deleteCourseResource(resourceId, loginUser);

        return ResultUtils.success(result);
    }

    @GetMapping("/get/vo/{courseId}")
    public BaseResponse<List<ResourceStudentVO>> getCourseResourceVO(@PathVariable("courseId") Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Course Id can not be empty!");
        }

        // 获取当前登录用户
        User loginUser = userService.getLoginUser();

        List<ResourceStudentVO> resourceStudentVOList = courseResourceService.getCourseResourceVO(courseId, loginUser);

        return ResultUtils.success(resourceStudentVOList);
    }
}
