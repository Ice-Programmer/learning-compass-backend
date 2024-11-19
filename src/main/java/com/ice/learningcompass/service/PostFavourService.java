package com.ice.learningcompass.service;

import com.ice.learningcompass.model.entity.PostFavour;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.learningcompass.model.entity.User;

/**
 * @author chenjiahan
 * @description 针对表【post_favour(帖子收藏)】的数据库操作Service
 * @createDate 2024-11-17 23:00:49
 */
public interface PostFavourService extends IService<PostFavour> {

    /**
     * 帖子收藏
     *
     * @param postId    帖子 id
     * @param loginUser 当前登录用户
     * @return 收藏状态
     */
    int doPostFavour(long postId, User loginUser);


    /**
     * 帖子收藏（内部服务）
     *
     * @param userId 用户 id
     * @param postId 帖子 id
     * @return 收藏状态
     */
    int doPostFavourInner(long userId, long postId);
}
