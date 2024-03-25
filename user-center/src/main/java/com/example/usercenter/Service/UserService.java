package com.example.usercenter.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercenter.Pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.usercenter.Pojo.request.UserUpdateInfoRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author cloudyW
* @description 针对表【user】的数据库操作Service
* @createDate 2024-03-03 16:13:54
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userName
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userName, String userAccount, String userPassword, String checkPassword, Integer gender);

    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originalUser
     * @return
     */
    User getSafetyUser(User originalUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 更新用户信息
     * @param updateInfoRequest
     * @return
     */
    User updateUserInfo(UserUpdateInfoRequest updateInfoRequest);

    /**
     * 重置密码
     */
    void resetPassword();
}
