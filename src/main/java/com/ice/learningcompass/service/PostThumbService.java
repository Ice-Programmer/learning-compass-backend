package com.ice.learningcompass.service;

import com.ice.learningcompass.model.entity.PostThumb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.learningcompass.model.entity.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chenjiahan
 * @description 针对表【post_thumb(帖子点赞)】的数据库操作Service
 * @createDate 2024-11-17 23:00:49
 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞
     *
     * @param postId    帖子 id
     * @param loginUser 当前登录用户
     * @return 点赞状态
     */
    int doPostThumb(long postId, User loginUser);

    /**
     * 封装了事务的方法
     *
     * @param userId 登录用户 id
     * @param postId 帖子 id
     * @return 点赞状态
     */
    @Transactional(rollbackFor = Exception.class)
    int doPostThumbInner(long userId, long postId);
}
