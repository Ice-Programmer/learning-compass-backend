package com.ice.learningcompass.model.dto.post;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子新增 Request
 */
@Data
public class PostAddRequest implements Serializable {

    /**
     * 评论名称
     */
    private String name;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 原帖id
     */
    private Long postId;

    /**
     * 帖子类型
     */
    private Integer postType;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tagList;

    /**
     * 图片列表
     */
    private List<String> pictureList;

    private static final long serialVersionUID = 1L;
}