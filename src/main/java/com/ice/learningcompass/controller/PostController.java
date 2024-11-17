package com.ice.learningcompass.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.learningcompass.common.BaseResponse;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.common.ResultUtils;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.model.dto.post.PostAddRequest;
import com.ice.learningcompass.model.dto.post.PostQueryRequest;
import com.ice.learningcompass.model.entity.Post;
import com.ice.learningcompass.model.entity.User;
import com.ice.learningcompass.model.vo.PostVO;
import com.ice.learningcompass.service.PostService;
import com.ice.learningcompass.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子 Controller
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/11/17 21:43
 */
@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    /**
     * 创建帖子
     *
     * @param postAddRequest 帖子新增请求
     * @return 帖子 id
     */
    @PostMapping("/add")
    public BaseResponse<Long> addPost(@RequestBody PostAddRequest postAddRequest) {
        if (postAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Post Info is Empty!");
        }

        // 获取登录用户
        User loginUser = userService.getLoginUser();

        Long postId = postService.addPost(postAddRequest, loginUser);

        return ResultUtils.success(postId);
    }

    /**
     * 获取帖子分页
     *
     * @param postQueryRequest 帖子请求 Request
     * @return 帖子分页
     */
    @PostMapping("/page/vo")
    public BaseResponse<Page<PostVO>> pagePostVO(@RequestBody PostQueryRequest postQueryRequest) {
        if (postQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser();
        postQueryRequest.setUserId(loginUser.getId());
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        Page<Post> postPage = postService.page(new Page<>(current, size),
                postService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(postService.getPostVOPage(postPage, loginUser));
    }

}
