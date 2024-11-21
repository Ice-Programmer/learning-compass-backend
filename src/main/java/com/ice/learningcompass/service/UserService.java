package com.ice.learningcompass.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ice.learningcompass.model.dto.user.UserAddRequest;
import com.ice.learningcompass.model.dto.user.UserQueryRequest;
import com.ice.learningcompass.model.dto.user.UserUpdateRequest;
import com.ice.learningcompass.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.learningcompass.model.vo.LoginUserVO;
import com.ice.learningcompass.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 获取脱敏的用户信息
     *
     * @param user 用户信息
     * @return 脱敏用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList 用户列表
     * @return 脱敏用户列表
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 创建用户（仅超级管理员）
     *
     * @param userAddRequest 用户新增请求
     * @return 用户id
     */
    Long addUser(UserAddRequest userAddRequest);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest 用户分页请求体
     * @return 查询条件
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    boolean changePassword(Long id, String originalPassword, String newPassword);
}
