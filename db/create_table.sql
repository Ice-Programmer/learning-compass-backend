# 数据库初始化
# @author <a href="https://github.com/IceProgramer">chenjiahan</a>
# @create 2024/10/14 21:13

-- 创建库
create database if not exists learning_compass;

-- 切换库
use learning_compass;

-- 用户表
create table if not exists `user`
(
    `id`           bigint auto_increment comment 'id' primary key,
    `userAccount`  varchar(256)                       not null comment '账号',
    `userPassword` varchar(512)                       not null comment '密码',
    `userName`     varchar(256)                       null comment '用户昵称',
    `email`        varchar(256)                       null comment '邮箱',
    `userAvatar`   varchar(1024)                      null comment '用户头像',
    `userProfile`  varchar(512)                       null comment '用户简介',
    `userRole`     varchar(256)                       not null comment '用户角色：student/teacher/admin/super-admin/ban',
    `createTime`   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`     tinyint  default 0                 not null comment '是否删除'
) comment '用户' collate = utf8mb4_unicode_ci;

-- 课程表
create table if not exists `course`
(
    `id`           bigint auto_increment comment 'id' primary key,
    `name`         varchar(256)                       null comment '课程名称',
    `description`  text                               null comment '描述',
    `introduction` text                               null comment '课程介绍',
    `picture`      varchar(2048)                      null comment '图片',
    `tags`         varchar(1024)                      null comment '标签列表（json 数组）',
    `teacherId`    bigint                             not null comment '创建教师 id',
    `status`       tinyint  default 0                 not null comment '课程状态：0-开启/1-关闭',
    `startTime`    datetime default CURRENT_TIMESTAMP not null comment '开始时间',
    `endTime`      datetime                           not null comment '结束时间',
    `createTime`   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`     tinyint  default 0                 not null comment '是否删除',
    index idx_teacherId (teacherId)
) comment '课程' collate = utf8mb4_unicode_ci;

-- 学生课程表
create table if not exists `course_student`
(
    `id`         bigint auto_increment comment 'id' primary key,
    `studentId`  bigint                             not null comment '学生 id',
    `courseId`   bigint                             not null comment '课程 id',
    `grade`      int      default 0                 not null comment '课程分数',
    `process`    int      default 0                 not null comment '学习进度（百分比表示）',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`   tinyint  default 0                 not null comment '是否删除',
    index idx_studentId (studentId),
    index idx_courseId (courseId),
    UNIQUE (studentId, courseId)
) comment '课程' collate = utf8mb4_unicode_ci;

-- 课程资料表
create table if not exists `course_resource`
(
    `id`           bigint auto_increment comment 'id' primary key,
    `teacherId`    bigint                             not null comment '创建教师 id',
    `courseId`     bigint                             not null comment '课程 id',
    `resourceName` varchar(256)                       null comment '资料名称',
    `resourceUrl`  varchar(1024)                      null comment '资料链接',
    `resourceType` tinyint                            null comment '课程资料',
    `createTime`   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`     tinyint  default 0                 not null comment '是否删除',
    index idx_teacherId (teacherId)
) comment '课程资料' collate = utf8mb4_unicode_ci;

-- 学生资料查看记录表
create table if not exists `resource_student`
(
    `id`         bigint auto_increment comment 'id' primary key,
    `resourceId` bigint                             not null comment '资料 id',
    `studentId`  bigint                             not null comment '查看学生 id',
    `viewNum`    int      default 0                 not null comment '查看次数',
    `viewTime`   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '查看时间',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`   tinyint  default 0                 not null comment '是否删除',
    index idx_studentId (studentId),
    UNIQUE (resourceId, studentId)
) comment '学生资料查看记录' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists `post`
(
    `id`         bigint auto_increment comment 'id' primary key,
    `name`       varchar(256)                       not null comment '评论名称',
    `content`    text                               null comment '评论内容',
    `courseId`   bigint                             null comment '课程id',
    `userId`     bigint                             null comment '用户id',
    `postId`     bigint                             null comment '原帖id',
    `tags`       varchar(1024)                      null comment '标签列表（json 数组）',
    `isReply`    tinyint  default 0                 not null comment '是否评论 0-原帖/1-评论贴',
    `postType`   tinyint  default 0                 not null comment '帖子类型',
    `viewNum`    int      default 0                 not null comment '浏览数',
    `thumbNum`   int      default 0                 not null comment '点赞数',
    `favourNum`  int      default 0                 not null comment '收藏数',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId),
    index courseId (courseId),
    index idx_postId (postId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    `id`         bigint auto_increment comment 'id' primary key,
    `postId`     bigint                             not null comment '帖子 id',
    `userId`     bigint                             not null comment '点赞用户 id',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞' collate = utf8mb4_unicode_ci;

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    `id`         bigint auto_increment comment 'id' primary key,
    `postId`     bigint                             not null comment '帖子 id',
    `userId`     bigint                             not null comment '创建用户 id',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏' collate = utf8mb4_unicode_ci;

-- 帖子图片列表
create table if not exists post_picture
(
    `id`         bigint auto_increment comment 'id' primary key,
    `postId`     bigint                             not null comment '帖子 id',
    `userId`     bigint                             not null comment '点赞用户 id',
    `picture`    varchar(1024)                      not null comment '图片',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`   tinyint  default 0                 not null comment '是否删除',
    index idx_postId (postId)
) comment '帖子图片' collate = utf8mb4_unicode_ci;
