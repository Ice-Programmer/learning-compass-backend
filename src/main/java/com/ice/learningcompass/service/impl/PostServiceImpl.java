package com.ice.learningcompass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.constant.CommonConstant;
import com.ice.learningcompass.constant.UserConstant;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.mapper.*;
import com.ice.learningcompass.model.dto.post.PostAddRequest;
import com.ice.learningcompass.model.dto.post.PostQueryRequest;
import com.ice.learningcompass.model.entity.*;
import com.ice.learningcompass.model.enums.PostReplyTypeEnum;
import com.ice.learningcompass.model.enums.PostTypeEnum;
import com.ice.learningcompass.model.vo.PostVO;
import com.ice.learningcompass.model.vo.UserVO;
import com.ice.learningcompass.service.PostPictureService;
import com.ice.learningcompass.service.PostService;
import com.ice.learningcompass.service.UserService;
import com.ice.learningcompass.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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
    private UserService userService;

    @Resource
    private PostPictureService pictureService;

    @Resource
    private PostThumbMapper postThumbMapper;

    @Resource
    private PostFavourMapper postFavourMapper;

    @Resource
    private PostPictureMapper postPictureMapper;

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
                PostReplyTypeEnum.NOT_REPLY.getValue() :
                PostReplyTypeEnum.IS_REPLY.getValue();
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

    @Override
    public QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        if (postQueryRequest == null) {
            return queryWrapper;
        }
        String searchText = postQueryRequest.getSearchText();
        String sortField = postQueryRequest.getSortField();
        String sortOrder = postQueryRequest.getSortOrder();
        Long id = postQueryRequest.getId();
        String name = postQueryRequest.getName();
        String content = postQueryRequest.getContent();
        List<String> tagList = postQueryRequest.getTagList();
        Long userId = postQueryRequest.getUserId();
        Long notId = postQueryRequest.getNotId();
        List<Long> ids = postQueryRequest.getIds();
        Long courseId = postQueryRequest.getCourseId();
        Long postId = postQueryRequest.getPostId();
        Set<Long> postIds = postQueryRequest.getPostIds();
        Integer isReply = postQueryRequest.getIsReply();
        Integer postType = postQueryRequest.getPostType();
        // todo 搜索收藏点赞相关用户
        Long favourUserId = postQueryRequest.getFavourUserId();
        Long thumbUserId = postQueryRequest.getThumbUserId();

        // 拼接查询条件
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("title", searchText).or().like("content", searchText);
        }
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        if (!CollectionUtils.isEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.in(!CollectionUtils.isEmpty(ids), "id", ids);
//        queryWrapper.in(!CollectionUtils.isEmpty(postIds), "postId", postIds);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(courseId), "courseId", courseId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(postId), "postId", postId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(postId), "postId", postId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(postType), "postType", postType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(isReply), "isReply", isReply);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<PostVO> getPostVOPage(Page<Post> postPage, User loginUser, Boolean isGetOrigin) {
        List<Post> postList = postPage.getRecords();
        Page<PostVO> postVOPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        if (CollectionUtils.isEmpty(postList)) {
            return postVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = postList.stream().map(Post::getUserId).collect(Collectors.toSet());
        Map<Long, List<UserVO>> userMap = userService.listByIds(userIdSet).stream()
                .map(user -> userService.getUserVO(user))
                .collect(Collectors.groupingBy(UserVO::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> postIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> postIdHasFavourMap = new HashMap<>();
        Set<Long> postIdSet = postList.stream().map(Post::getId).collect(Collectors.toSet());

        // 获取点赞
        List<PostThumb> postPostThumbList = postThumbMapper.selectList(
                Wrappers.<PostThumb>lambdaQuery()
                        .in(PostThumb::getPostId, postIdSet)
                        .eq(PostThumb::getUserId, loginUser.getId())
        );
        postPostThumbList.forEach(postPostThumb -> postIdHasThumbMap.put(postPostThumb.getPostId(), true));
        // 获取收藏
        List<PostFavour> postFavourList = postFavourMapper.selectList(
                Wrappers.<PostFavour>lambdaQuery()
                        .eq(PostFavour::getUserId, loginUser.getId())
                        .in(PostFavour::getPostId, postIdSet)
        );
        postFavourList.forEach(postFavour -> postIdHasFavourMap.put(postFavour.getPostId(), true));
        // 获取图片
        List<PostPicture> pictureList = postPictureMapper.selectList(
                Wrappers.<PostPicture>lambdaQuery()
                        .in(PostPicture::getPostId, postIdSet)
                        .select(PostPicture::getPostId, PostPicture::getPicture)
        );
        Map<Long, List<PostPicture>> pictureMap = pictureList.stream()
                .collect(Collectors.groupingBy(PostPicture::getPostId));

        // 填充信息
        List<PostVO> postVOList = postList.stream().map(post -> {
            PostVO postVO = PostVO.objToVo(post);
            Long userId = post.getUserId();
            UserVO userVO = null;
            if (userMap.containsKey(userId)) {
                userVO = userMap.get(userId).get(0);
            }
            postVO.setUserInfo(userVO);
            postVO.setHasThumb(postIdHasThumbMap.getOrDefault(post.getId(), false));
            postVO.setHasFavour(postIdHasFavourMap.getOrDefault(post.getId(), false));
            List<PostPicture> postPictureList = pictureMap.getOrDefault(post.getId(), Collections.emptyList());
            postVO.setPictureList(postPictureList.stream().map(PostPicture::getPicture).collect(Collectors.toList()));
            return postVO;
        }).collect(Collectors.toList());
        postVOPage.setRecords(postVOList);
        return postVOPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePost(Long postId, User loginUser) {
        // 判断帖子是否存在
        Post post = baseMapper.selectOne(Wrappers.<Post>lambdaQuery()
                .eq(Post::getId, postId)
                .select(Post::getId, Post::getUserId)
                .last("limit 1"));
        ThrowUtils.throwIf(post == null, ErrorCode.OPERATION_ERROR, "Delete post Not Found!");

        // 判断是否有权限删除
        hasPostRole(post, loginUser);

        // 删除帖子信息
        boolean result = baseMapper.deleteById(postId) != 0;
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "Delete post ERROR!");
        log.info("Delete Post {} by User {}", postId, loginUser.getId());

        // 删除帖子图片
        int pictureNum = postPictureMapper.delete(Wrappers.<PostPicture>lambdaQuery()
                .eq(PostPicture::getPostId, postId));
        ThrowUtils.throwIf(pictureNum <= 0, ErrorCode.OPERATION_ERROR, "Delete post Picture ERROR!");
        log.info("Delete {} Post pictures by User {}", pictureNum, loginUser.getId());

        return true;
    }

    private void validatePost(Post post, boolean add) {
        if (post == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = post.getName();
        String content = post.getContent();
        Long postId = post.getPostId();
        Long courseId = post.getCourseId();
        Integer postType = post.getPostType();

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
        // 校验帖子类型
        if (postType != null) {
            if (!PostTypeEnum.getValues().contains(postType)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Post Type Error!");
            }
        }
    }

    /**
     * 判断是否拥有操作权限（仅创建者和管理员）
     *
     * @param post      帖子
     * @param loginUser 登录用户
     */
    private void hasPostRole(Post post, User loginUser) {
        if (!post.getUserId().equals(loginUser.getId()) &&
                !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole()) &&
                !UserConstant.SUPER_ADMIN_ROLE.equals(loginUser.getUserRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "Only Post Creator or Admin can delete!");
        }
    }


}




