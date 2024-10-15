package com.ice.learningcompass.service;

import com.ice.learningcompass.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author chenjiahan
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2024-10-15 09:19:42
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param userRole      用户身份
     * @return 新用户 id
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword, String userRole);
}
