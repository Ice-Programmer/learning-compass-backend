package com.ice.learningcompass.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.learningcompass.model.dto.post.PostAddRequest;
import com.ice.learningcompass.model.dto.post.PostQueryRequest;
import com.ice.learningcompass.model.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.learningcompass.model.entity.User;
import com.ice.learningcompass.model.vo.PostVO;

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

    /**
     * 搜索帖子条件
     *
     * @param postQueryRequest 搜索请求
     * @return 搜索条件
     */
    QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest);

    /**
     * 封装帖子VO分页
     *
     * @param postPage  帖子分页
     * @param loginUser 当前登录用户
     * @return 帖子VO分页
     */
    Page<PostVO> getPostVOPage(Page<Post> postPage, User loginUser);
}
