package com.example.usercenter.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercenter.Constant.ErrorCode;
import com.example.usercenter.Pojo.User;
import com.example.usercenter.Pojo.request.AdminSearchRequest;
import com.example.usercenter.Pojo.request.UserLoginRequest;
import com.example.usercenter.Pojo.request.UserRegisterRequest;
import com.example.usercenter.Pojo.request.UserUpdateInfoRequest;
import com.example.usercenter.Service.UserService;
import com.example.usercenter.Pojo.Result;
import com.example.usercenter.Utils.AccountGenerator;
import com.example.usercenter.Utils.AliOSSUtils;
import com.example.usercenter.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.usercenter.Constant.UserConstant.ADMIN_ROLE;
import static com.example.usercenter.Constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 * @author CloudyW
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountGenerator accountGenerator;

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null) {
            return Result.error(ErrorCode.PARAMS_ERROR);
        }
        String userName = userRegisterRequest.getUserName();
        String userAccount = accountGenerator.generateAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        Integer gender = userRegisterRequest.getGender();
        if (StringUtils.isAnyBlank(userName, userPassword, checkPassword)) {
            return Result.error(ErrorCode.USER_INFO_ERROR);
        }
        long result = userService.userRegister(userName, userAccount, userPassword, checkPassword, gender);
        return Result.success(result, "用户插入成功，id:" + result);
    }

    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户登录信息为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.USER_INFO_ERROR, "账号或密码为空");
        }
        return Result.success(userService.userLogin(userAccount, userPassword, request), userAccount + "登录成功");
    }

    @PostMapping("/logout")
    public Result userLogout(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户未登录");
        }
        return Result.success(userService.userLogout(request));
    }

    @GetMapping("/current")
    public Result getCurrentUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户未登录");
        }
        // todo 校验用户是否合法
        User currentUser = userService.getById(user.getId());
        if (currentUser.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在");
        }
        return Result.success(userService.getSafetyUser(currentUser));
    }


    @PostMapping("/search")
    public Result searchUsers(@RequestBody AdminSearchRequest adminSearchRequest, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(adminSearchRequest != null) {
            String username = adminSearchRequest.getUserName();
            String userAccount = adminSearchRequest.getUserAccount();
            Integer gender = adminSearchRequest.getGender();
            String phone = adminSearchRequest.getPhone();
            String email = adminSearchRequest.getEmail();
            if (username != null) {
                queryWrapper.like("username", username);
            }
            if (userAccount != null) {
                queryWrapper.like("user_account", userAccount);
            }
            if (gender != null) {
                queryWrapper.eq("gender", gender);
            }
            if (phone != null) {
                queryWrapper.like("phone", phone);
            }
            if (email != null) {
                queryWrapper.like("email", email);
            }
        }
        List<User> userList = userService.list(queryWrapper);
        return Result.success(userList.stream().map(user -> userService.getSafetyUser(user)).toList());
    }

    @PostMapping("/getAvatar")
    public Result uploadAvatar(@RequestParam("image")MultipartFile avatar, HttpServletRequest request) throws IOException, ClientException {
        if (avatar == null) {
            return Result.error(ErrorCode.NULL_ERROR, "请求发生错误");
        }
        String avatarURL = aliOSSUtils.upload(avatar);
        return Result.success(avatarURL);
    }

    @PostMapping("/updateInfo")
    public Result updateUserInfo(@RequestBody UserUpdateInfoRequest updateInfoRequest, HttpServletRequest request) {
        if (updateInfoRequest == null) {
            return Result.error(ErrorCode.NULL_ERROR, "请求发生错误");
        }
        User user = userService.updateUserInfo(updateInfoRequest);
        return Result.success(user);
    }

    @PostMapping("/delete")
    public Result deleteUser(long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return Result.error(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            return Result.error(ErrorCode.USER_NOT_EXIST);
        }
        return userService.removeById(id) ? Result.success() : Result.error(ErrorCode.USER_NOT_EXIST);
    }

    /**
     * 判断是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
