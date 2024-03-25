package com.example.usercenter.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenter.Constant.ErrorCode;
import com.example.usercenter.Pojo.Result;
import com.example.usercenter.Pojo.User;
import com.example.usercenter.Pojo.request.UserUpdateInfoRequest;
import com.example.usercenter.Service.UserService;
import com.example.usercenter.Mapper.UserMapper;
import com.example.usercenter.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.regex.Pattern;

import static com.example.usercenter.Constant.UserConstant.USER_LOGIN_STATE;

/**
* @author cloudyW
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-03-03 16:13:54
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "cloudyW";

    @Override
    public long userRegister(String username, String userAccount, String userPassword, String checkPassword, Integer gender) {
        //1. 校验
        if (StringUtils.isAnyBlank(username, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.USER_INFO_ERROR, "用户名或密码有空格");
        }
        //用户名不能包含特殊字符，4-16位
        String usernameLegalPattern = "^\\w{4,16}$";//"^[_a-zA-Z0-9]{4,16}$"
        if (!Pattern.matches(usernameLegalPattern, username)) {
            throw new BusinessException(ErrorCode.USER_INFO_ERROR, "用户名格式错误");
        }
        //密码不能包含特殊字符，6-16位
        String passwordLegalPattern = "^\\w{6,16}$";
        if (!Pattern.matches(passwordLegalPattern, userPassword)) {
            throw new BusinessException(ErrorCode.USER_INFO_ERROR, "密码格式错误");
        }
        //两次输入密码不一致
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.USER_INFO_ERROR, "两次输入密码不一致");
        }
        //账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.USER_DUPLICATE, "账号重复");
        }

        //2. 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3. 数据库插入数据
        User user = new User();
        user.setUsername(username);
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setGender(gender);
        user.setAvatarUrl("");
        user.setCreateTime(new Date());
        boolean savaResult = this.save(user);
        if (!savaResult) {
            throw new BusinessException(ErrorCode.DATA_BASE_ERROR, "数据库存储错误");
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.USER_INFO_ERROR, "用户名或密码有空格");
        }
        //账户不能包含特殊字符，4-16位
        String accountLegalPattern = "^\\d{10}$";//"^[_a-zA-Z0-9]{5,16}$"
        if (!Pattern.matches(accountLegalPattern, userAccount)) {
            throw new BusinessException(ErrorCode.USER_INFO_ERROR, "账号格式错误");
        }
        //密码不能包含特殊字符，6-16位
        String passwordLegalPattern = "^\\w{6,16}$";
        if (!Pattern.matches(passwordLegalPattern, userPassword)) {
            throw new BusinessException(ErrorCode.USER_INFO_ERROR, "密码格式错误");
        }
        //2. 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3. 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //账号密码不匹配或用户不存在
        if (user == null) {
            log.info("user login failed, user not exist or userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.USER_INFO_ERROR, "账号密码不匹配或用户不存在");
        }
        //4. 用户脱敏
        User safetyUser = getSafetyUser(user);
        //5. 记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);

        return safetyUser;
    }

    @Override
    public User getSafetyUser(User originalUser) {
        if (originalUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户为空");
        }
        User safetyUser = new User();
        safetyUser.setId(originalUser.getId());
        safetyUser.setUsername(originalUser.getUsername());
        safetyUser.setUserAccount(originalUser.getUserAccount());
        safetyUser.setAvatarUrl(originalUser.getAvatarUrl());
        safetyUser.setGender(originalUser.getGender());
        safetyUser.setPhone(originalUser.getPhone());
        safetyUser.setEmail(originalUser.getEmail());
        safetyUser.setUserRole(originalUser.getUserRole());
        safetyUser.setUserStatus(originalUser.getUserStatus());
        safetyUser.setCreateTime(originalUser.getCreateTime());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public User updateUserInfo(UserUpdateInfoRequest updateInfoRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", updateInfoRequest.getUserAccount());
        User user = userMapper.selectOne(queryWrapper);
        if (updateInfoRequest.getUsername() != null && updateInfoRequest.getUsername() != "") {
            user.setUsername(updateInfoRequest.getUsername());
        } else {
            throw new BusinessException(ErrorCode.USER_INFO_ERROR, "用户名不能为空");
        }
        if (updateInfoRequest.getAvatar() != null && updateInfoRequest.getAvatar() != "") {
            user.setAvatarUrl(updateInfoRequest.getAvatar());
        }
        user.setGender(updateInfoRequest.getGender());
        user.setPhone(updateInfoRequest.getPhone());
        user.setEmail(updateInfoRequest.getEmail());
        return getSafetyUser(user);
    }

    @Override
    public void resetPassword() {
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + "123456").getBytes());
        User user = new User();
        user.setUserPassword(encryptPassword);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        int id = userMapper.update(user, queryWrapper);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.DATA_BASE_ERROR, "用户删除失败");
        }
    }
}




