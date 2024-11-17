package com.ice.learningcompass.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.model.entity.PostPicture;
import com.ice.learningcompass.model.entity.User;
import com.ice.learningcompass.service.PostPictureService;
import com.ice.learningcompass.mapper.PostPictureMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenjiahan
 * @description 针对表【post_picture(帖子图片)】的数据库操作Service实现
 * @createDate 2024-11-17 22:06:25
 */
@Service
public class PostPictureServiceImpl extends ServiceImpl<PostPictureMapper, PostPicture>
        implements PostPictureService {

    @Override
    public int addPictureList(List<String> pictureList, Long postId, User loginUser) {
        List<PostPicture> postPictureList = pictureList.stream().map(picture -> {
            PostPicture postPicture = new PostPicture();
            postPicture.setPostId(postId);
            postPicture.setPicture(picture);
            postPicture.setUserId(loginUser.getId());
            return postPicture;
        }).collect(Collectors.toList());

        int pictureNum = baseMapper.insetPictureList(postPictureList);
        ThrowUtils.throwIf(pictureNum <= 0, ErrorCode.OPERATION_ERROR, "Save Post's picture ERROR!");
        return pictureNum;
    }
}




