-- Database initialization
-- @author <a href="https://github.com/IceProgramer">chenjiahan</a>
-- @create 2024/10/14 21:13

-- Create database
create database if not exists learning_compass_en;

-- Switch database
use learning_compass_en;

-- User table
create table if not exists `user`
(
    `id`           bigint auto_increment comment 'ID' primary key,
    `userAccount`  varchar(256)                       not null comment 'Account',
    `userPassword` varchar(512)                       not null comment 'Password',
    `userName`     varchar(256)                       null comment 'User Nickname',
    `email`        varchar(256)                       null comment 'Email',
    `userAvatar`   varchar(1024)                      null comment 'User Avatar',
    `userProfile`  varchar(512)                       null comment 'User Profile',
    `userRole`     varchar(256)                       not null comment 'User Role: student/teacher/admin/super-admin/ban',
    `createTime`   datetime default CURRENT_TIMESTAMP not null comment 'Creation Time',
    `updateTime`   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update Time',
    `isDelete`     tinyint  default 0                 not null comment 'Is Deleted'
) comment 'User' collate = utf8mb4_unicode_ci;

-- Course table
create table if not exists `course`
(
    `id`          bigint auto_increment comment 'ID' primary key,
    `name`        varchar(256)                       null comment 'Course Name',
    `description` text                               null comment 'Description',
    `picture`     varchar(2048)                      null comment 'Picture',
    `userAvatar`  varchar(1024)                      null comment 'User Avatar',
    `tags`        varchar(1024)                      null comment 'Tags (JSON Array)',
    `teacherId`   bigint                             not null comment 'Teacher ID',
    `status`      tinyint                            not null comment 'Course Status: 0-Open/1-Closed',
    `startTime`   datetime default CURRENT_TIMESTAMP not null comment 'Start Time',
    `endTime`     datetime                           not null comment 'End Time',
    `createTime`  datetime default CURRENT_TIMESTAMP not null comment 'Creation Time',
    `updateTime`  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update Time',
    `isDelete`    tinyint  default 0                 not null comment 'Is Deleted',
    index idx_teacherId (teacherId)
) comment 'Course' collate = utf8mb4_unicode_ci;

-- Student Course table
create table if not exists `course_student`
(
    `id`         bigint auto_increment comment 'ID' primary key,
    `studentId`  bigint                             not null comment 'Student ID',
    `courseId`   bigint                             not null comment 'Course ID',
    `grade`      int      default 0                 not null comment 'Course Grade',
    `process`    int      default 0                 not null comment 'Progress (Percentage)',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment 'Creation Time',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update Time',
    `isDelete`   tinyint  default 0                 not null comment 'Is Deleted',
    index idx_studentId (studentId),
    index idx_courseId (courseId),
    UNIQUE (studentId, courseId)
) comment 'Student Course' collate = utf8mb4_unicode_ci;

-- Course Resource table
create table if not exists `resource_student`
(
    `id`           bigint auto_increment comment 'ID' primary key,
    `teacherId`    bigint                             not null comment 'Teacher ID',
    `courseId`     bigint                             not null comment 'Course ID',
    `resourceName` varchar(256)                       null comment 'Resource Name',
    `resourceUrl`  varchar(1024)                      null comment 'Resource URL',
    `createTime`   datetime default CURRENT_TIMESTAMP not null comment 'Creation Time',
    `updateTime`   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update Time',
    `isDelete`     tinyint  default 0                 not null comment 'Is Deleted',
    index idx_teacherId (teacherId)
) comment 'Course Resource' collate = utf8mb4_unicode_ci;

-- Student Resource View Record table
create table if not exists `course_resource`
(
    `id`         bigint auto_increment comment 'ID' primary key,
    `resourceId` bigint                             not null comment 'Resource ID',
    `studentId`  bigint                             not null comment 'Viewing Student ID',
    `viewTime`   datetime default CURRENT_TIMESTAMP not null comment 'View Time',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment 'Creation Time',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update Time',
    `isDelete`   tinyint  default 0                 not null comment 'Is Deleted',
    index idx_studentId (studentId)
) comment 'Student Resource View Record' collate = utf8mb4_unicode_ci;
