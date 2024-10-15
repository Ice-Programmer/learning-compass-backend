package com.ice.learningcompass.model.dto.user;

import lombok.Data;

/**
 * 用户更新请求
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/15 12:10
 */
@Data
public class UserUpdateRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：student/teacher/admin/super-admin
     */
    private String userRole;

}
