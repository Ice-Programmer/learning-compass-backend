package com.ice.learningcompass.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.ice.learningcompass.common.BaseResponse;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.common.ResultUtils;
import com.ice.learningcompass.constant.UserConstant;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.model.dto.coursestudent.StudentJoinCourseRequest;
import com.ice.learningcompass.model.entity.User;
import com.ice.learningcompass.service.CourseStudentService;
import com.ice.learningcompass.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/16 12:14
 */
@Slf4j
@RestController
@RequestMapping("/course/student")
public class CourseStudentController {

    @Resource
    private CourseStudentService courseStudentService;

    @Resource
    private UserService userService;

    /**
     * 学生加入课程
     *
     * @param studentJoinCourseRequest 加入课程请求体
     * @return 加入课程记录 id
     */
    @PostMapping("/join")
    @SaCheckRole(UserConstant.STUDENT_ROLE)
    public BaseResponse<Long> studentJoinCourse(@RequestBody StudentJoinCourseRequest studentJoinCourseRequest) {
        ThrowUtils.throwIf(studentJoinCourseRequest == null, ErrorCode.PARAMS_ERROR);
        Long courseId = studentJoinCourseRequest.getCourseId();
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Course Id is empty!");
        }
        // 获取当前登录用户 id
        User loginUser = userService.getLoginUser();

        Long joinId = courseStudentService.studentJoinCourse(courseId, loginUser.getId());

        // todo 发布邮件给教师 「学生加入课程」

        return ResultUtils.success(joinId);
    }
}
