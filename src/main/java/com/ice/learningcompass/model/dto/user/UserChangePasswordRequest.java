package com.ice.learningcompass.model.dto.user;

import lombok.Data;

/**
 * 用户更改密码操作
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/11/19 21:35
 */
@Data
public class UserChangePasswordRequest {
    /**
     * 原有密码
     */
    private String originalPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认密码
     */
    private String checkPassword;
}
