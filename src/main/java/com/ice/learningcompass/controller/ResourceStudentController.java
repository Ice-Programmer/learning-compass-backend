package com.ice.learningcompass.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.ice.learningcompass.common.BaseResponse;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.common.ResultUtils;
import com.ice.learningcompass.constant.UserConstant;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.model.dto.resourcestudent.StudentViewResourceRequest;
import com.ice.learningcompass.model.entity.User;
import com.ice.learningcompass.service.ResourceStudentService;
import com.ice.learningcompass.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/17 19:32
 */
@Slf4j
@RestController
@RequestMapping("/resource/student")
public class ResourceStudentController {

    @Resource
    private ResourceStudentService resourceStudentService;

    @Resource
    private UserService userService;

    /**
     * 学生访问课程资料
     *
     * @param studentViewResourceRequest 学生访问课程资料请求
     * @return 访问次数
     */
    @PostMapping("/view")
    @SaCheckRole(UserConstant.STUDENT_ROLE)
    public BaseResponse<Integer> studentViewResource(@RequestBody StudentViewResourceRequest studentViewResourceRequest) {
        ThrowUtils.throwIf(studentViewResourceRequest == null, ErrorCode.PARAMS_ERROR);
        Long resourceId = studentViewResourceRequest.getResourceId();
        if (resourceId == null || resourceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Resource Id not empty!");
        }

        // 获取当前登录用户
        User loginUser = userService.getLoginUser();

        Integer viewNum = resourceStudentService.studentViewResource(resourceId, loginUser.getId());

        return ResultUtils.success(viewNum);
    }

}
