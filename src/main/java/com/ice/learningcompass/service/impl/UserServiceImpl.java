package com.ice.learningcompass.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.constant.AvatarDefaultConstant;
import com.ice.learningcompass.constant.UserConstant;
import com.ice.learningcompass.exception.BusinessException;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.model.entity.User;
import com.ice.learningcompass.model.enums.UserRoleEnum;
import com.ice.learningcompass.service.UserService;
import com.ice.learningcompass.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author chenjiahan
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-10-15 09:19:42
 */
@Service
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




