package com.ice.learningcompass.model.vo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ice.learningcompass.model.entity.Post;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子
 *
 * @TableName post
 */
@Data
public class PostVO implements Serializable {

    private static final Gson GSON = new Gson();

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
    private UserVO userInfo;

    /**
     * 原帖id
     */
    private Long postId;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tagList;

    /**
     * 图片列表
     */
    private List<String> pictureList;

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
     * 是否点赞
     */
    private Boolean hasThumb;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 是否收藏
     */
    private Boolean hasFavour;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param postVO 帖子包装类
     * @return
     */
    public static Post voToObj(PostVO postVO) {
        if (postVO == null) {
            return null;
        }
        Post post = new Post();
        BeanUtils.copyProperties(postVO, post);
        List<String> tagList = postVO.getTagList();
        if (tagList != null) {
            post.setTags(GSON.toJson(tagList));
        }
        return post;
    }

    /**
     * 对象转包装类
     *
     * @param post 帖子
     * @return
     */
    public static PostVO objToVo(Post post) {
        if (post == null) {
            return null;
        }
        PostVO postVO = new PostVO();
        BeanUtils.copyProperties(post, postVO);
        postVO.setTagList(GSON.fromJson(post.getTags(), new TypeToken<List<String>>() {
        }.getType()));
        return postVO;
    }
}