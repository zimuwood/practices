package com.example.usercenter.Pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 * @author CloudyW
 * @version 1.0
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -3527533295411265559L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
