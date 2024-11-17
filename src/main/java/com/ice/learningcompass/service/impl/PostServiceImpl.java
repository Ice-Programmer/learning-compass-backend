package com.ice.learningcompass.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.mapper.CourseMapper;
import com.ice.learningcompass.model.dto.post.PostAddRequest;
import com.ice.learningcompass.model.entity.Course;
import com.ice.learningcompass.model.entity.Post;
import com.ice.learningcompass.model.entity.User;
import com.ice.learningcompass.model.enums.PostTypeEnum;
import com.ice.learningcompass.service.PostPictureService;
import com.ice.learningcompass.service.PostService;
import com.ice.learningcompass.mapper.PostMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenjiahan
 * @description 针对表【post(帖子)】的数据库操作Service实现
 * @createDate 2024-11-17 21:42:28
 */
@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
        implements PostService {

    private static final Gson GSON = new Gson();

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private PostPictureService pictureService;

    @Override
    public Long addPost(PostAddRequest postAddRequest, User loginUser) {
        // 校验参数
        Post post = new Post();
        BeanUtils.copyProperties(postAddRequest, post);
        post.setTags(GSON.toJson(postAddRequest.getTagList()));
        validatePost(post, true);

        // 补充
        post.setUserId(loginUser.getId());
        int isReply = post.getPostId() == null ?
                PostTypeEnum.NOT_REPLY.getValue() :
                PostTypeEnum.IS_REPLY.getValue();
        post.setIsReply(isReply);

        AtomicLong postId = new AtomicLong();
        // 插入数据库
        transactionTemplate.execute(status -> {
            boolean result = baseMapper.insert(post) != 0;
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "Post Error!");
            log.info("insert post {} by user {}", post.getName(), loginUser.getId());
            List<String> pictureList = postAddRequest.getPictureList();
            // 保存图片信息
            if (!pictureList.isEmpty()) {
                int pictureNum = pictureService.addPictureList(pictureList, post.getId(), loginUser);
                log.info("insert post picture num {} by user {}, {}", pictureNum, loginUser.getId(), GSON.toJson(pictureList));
            }
            postId.set(post.getId());
            return Boolean.TRUE;
        });
        return postId.longValue();
    }

    private void validatePost(Post post, boolean add) {
        if (post == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = post.getName();
        String content = post.getContent();
        Long postId = post.getPostId();
        Long courseId = post.getCourseId();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR, "Post Title can Not be empty!");
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Post title is too long!");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Post content is too long!");
        }
        // 校验原帖是否存在
        if (postId != null) {
            ThrowUtils.throwIf(postId <= 0, ErrorCode.PARAMS_ERROR, "Origin Post Id must more than 0!");
            boolean exists = baseMapper.exists(Wrappers.<Post>lambdaQuery()
                    .eq(Post::getId, postId));
            ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "Reply Post must has the origin Post!");
        }
        // 校验课程是否存在
        if (courseId != null) {
            ThrowUtils.throwIf(courseId <= 0, ErrorCode.PARAMS_ERROR, "Course Id must more than 0!");
            boolean exists = courseMapper.exists(Wrappers.<Course>lambdaQuery()
                    .eq(Course::getId, courseId));
            ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "Course Post must has relative course!");
        }
    }
}




