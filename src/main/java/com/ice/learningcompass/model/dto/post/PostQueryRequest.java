package com.ice.learningcompass.model.dto.post;

import com.ice.learningcompass.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子查询 Request
 *
 * @TableName post
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PostQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * ids
     */
    private List<Long> ids;

    /**
     * 评论名称
     */
    private String name;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 关键词
     */
    private String searchText;

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
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 是否评论 0-原帖/1-评论贴
     */
    private Integer isReply;

    /**
     * 帖子类型
     */
    private Integer postType;

    /**
     * notId
     */
    private Long notId;

    /**
     * 收藏用户 id
     */
    private Long favourUserId;

    /**
     * 点赞用户 id
     */
    private Long thumbUserId;

    private static final long serialVersionUID = 1L;
}