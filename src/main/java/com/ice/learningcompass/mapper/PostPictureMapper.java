package com.ice.learningcompass.mapper;

import com.ice.learningcompass.model.entity.PostPicture;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenjiahan
 * @description 针对表【post_picture(帖子图片)】的数据库操作Mapper
 * @createDate 2024-11-17 22:06:25
 * @Entity com.ice.learningcompass.model.entity.PostPicture
 */
public interface PostPictureMapper extends BaseMapper<PostPicture> {

    /**
     * 批量插入图片
     *
     * @param postPictureList 图片列表
     * @return 插入数量
     */
    int insetPictureList(@Param("postPictureList") List<PostPicture> postPictureList);
}




