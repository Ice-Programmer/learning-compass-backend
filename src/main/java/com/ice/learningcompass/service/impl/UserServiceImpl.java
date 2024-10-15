package com.ice.learningcompass.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.constant.AvatarDefaultConstant;
import com.ice.learningcompass.constant.UserConstant;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.model.dto.user.UserAddRequest;
import com.ice.learningcompass.model.entity.User;
import com.ice.learningcompass.model.enums.UserRoleEnum;
import com.ice.learningcompass.model.vo.LoginUserVO;
import com.ice.learningcompass.service.UserService;
import com.ice.learningcompass.mapper.UserMapper;
import com.ice.learningcompass.utils.DeviceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

import static com.ice.learningcompass.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author chenjiahan
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-10-15 09:19:42
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "ice";


    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword, String userRole) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Parameter is empty!");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "User account is too short!");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "User password is too short!");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The passwords entered do not align with one another!");
        }
        // 判断用户身份是否存在
        if (!UserRoleEnum.getValues().contains(userRole)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The user role is invalid!");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account already exists!");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            String userName = UserConstant.USER_NAME_PREFIX + RandomUtil.randomString(6);
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setUserAvatar(getRandomAvatar(userRole));
            user.setUserName(userName);
            user.setUserRole(userRole);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Registration failed due to a database error!");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        User user = this.baseMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword));
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        // 使用 Sa-Token 登陆，并指定设备，同端登陆互斥
        StpUtil.login(user.getId(), DeviceUtils.getRequestDevice(request));
        StpUtil.getSession().set(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public User getLoginUser() {
        // 先判断是否已登录
        Object loginUserId = StpUtil.getLoginIdDefaultNull();
        if (loginUserId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        User currentUser = this.getById((String) loginUserId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public boolean userLogout() {
        StpUtil.checkLogin();
        // 移除登陆态
        StpUtil.logout();
        return true;
    }

    @Override
    public Long addUser(UserAddRequest userAddRequest) {
        // 1. 校验
        String userRole = userAddRequest.getUserRole();
        String userAccount = userAddRequest.getUserAccount();
        if (!UserRoleEnum.getValues().contains(userRole)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The user role is invalid!");
        }
        if (StringUtils.isBlank(userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The user account is empty!");
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 设置头像
        if (user.getUserAvatar() == null) {
            user.setUserAvatar(getRandomAvatar(userRole));
        }
        // 设置用户名
        if (user.getUserName() == null) {
            String userName = UserConstant.USER_NAME_PREFIX + RandomUtil.randomString(6);
            user.setUserName(userName);
        }
        // 默认密码 12345678
        String defaultPassword = "12345678";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);
        boolean result = this.baseMapper.insert(user) != 0;
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return user.getId();
    }

    /**
     * 生成随机头像
     *
     * @param userRole 用户身份
     * @return 头像
     **/
    private String getRandomAvatar(String userRole) {
        String randomAvatar = null;
        if (UserConstant.STUDENT_ROLE.equals(userRole)) {
            // 随机选择一个 student 头像
            int avatarIndex = RandomUtil.randomInt(AvatarDefaultConstant.DEFAULT_EMPLOYEE_AVATAR.length);
            randomAvatar = AvatarDefaultConstant.DEFAULT_EMPLOYEE_AVATAR[avatarIndex];
        }
        if (UserConstant.TEACHER_ROLE.equals(userRole)) {
            // 随机选择一个 teacher 头像
            int avatarIndex = RandomUtil.randomInt(AvatarDefaultConstant.DEFAULT_EMPLOYER_AVATAR.length);
            randomAvatar = AvatarDefaultConstant.DEFAULT_EMPLOYER_AVATAR[avatarIndex];
        }
        if (UserConstant.ADMIN_ROLE.equals(userRole) || UserConstant.SUPER_ADMIN_ROLE.equals(userRole)) {
            randomAvatar = AvatarDefaultConstant.DEFAULT_ADMIN_AVATAR;
        }
        ThrowUtils.throwIf(StringUtils.isBlank(randomAvatar), ErrorCode.PARAMS_ERROR, "The user role is invalid");
        return randomAvatar;
    }
}




