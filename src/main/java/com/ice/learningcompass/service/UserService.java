package com.ice.learningcompass.service;

import com.ice.learningcompass.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.learningcompass.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      session
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 获取脱敏的已登录用户信息
     *
     * @return 脱敏的已登录用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取当前登录用户
     *
     * @return 当前登陆用户信息
     */
    User getLoginUser();

    /**
     * 用户注销
     *
     * @return 注销成功
     */
    boolean userLogout();
}
