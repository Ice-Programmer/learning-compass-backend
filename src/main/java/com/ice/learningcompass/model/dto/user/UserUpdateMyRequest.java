package com.ice.learningcompass.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新个人信息请求
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/15 13:13
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 邮箱
     */
    private String email;

    private static final long serialVersionUID = 1L;
}
