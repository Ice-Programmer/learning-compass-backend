package com.ice.learningcompass.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 帖子
 *
 * @TableName post
 */
@Data
public class PostVO implements Serializable {
    /**
     * id
     */
    private Long id;

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
     * 用户id
     */
    private Long userId;

    /**
     * 原帖id
     */
    private Long postId;

    /**
     * 标签列表（json 数组）
     */
    private String tags;

    /**
     * 是否评论 0-原帖/1-评论贴
     */
    private Integer isReply;

    /**
     * 浏览数
     */
    private Integer viewNum;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}