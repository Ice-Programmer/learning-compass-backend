package com.ice.learningcompass.service;

import com.ice.learningcompass.model.entity.PostPicture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.learningcompass.model.entity.User;

import java.util.List;

/**
 * @author chenjiahan
 * @description 针对表【post_picture(帖子图片)】的数据库操作Service
 * @createDate 2024-11-17 22:06:25
 */
public interface PostPictureService extends IService<PostPicture> {

    /**
     * 保存帖子图片列表
     *
     * @param pictureList 帖子图片列表
     * @param postId      帖子 id
     * @param loginUser   登录用户
     * @return
     */
    int addPictureList(List<String> pictureList, Long postId, User loginUser);
}
