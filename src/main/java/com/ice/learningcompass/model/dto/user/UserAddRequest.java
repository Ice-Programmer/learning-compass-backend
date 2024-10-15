package com.ice.learningcompass.model.dto.user;

import io.swagger.annotations.Api;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户新增请求体
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/15 11:29
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色: student, teacher, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
