package com.ice.learningcompass.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/15 11:00
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String userPassword;
}
