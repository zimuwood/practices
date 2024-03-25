package com.example.usercenter.Pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 * @author CloudyW
 * @version 1.0
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -6031998278409940852L;

    private String userName;
    private String userPassword;
    private String checkPassword;
    private Integer gender;
}
