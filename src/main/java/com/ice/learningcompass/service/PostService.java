package com.ice.learningcompass.service;

import com.ice.learningcompass.model.dto.post.PostAddRequest;
import com.ice.learningcompass.model.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.learningcompass.model.entity.User;

/**
 * @author chenjiahan
 * @description 针对表【post(帖子)】的数据库操作Service
 * @createDate 2024-11-17 21:42:28
 */
public interface PostService extends IService<Post> {

    /**
     * 创建帖子
     *
     * @param postAddRequest 帖子新增请求
     * @param loginUser      当前登录用户
     * @return 帖子 id
     */
    Long addPost(PostAddRequest postAddRequest, User loginUser);
}
