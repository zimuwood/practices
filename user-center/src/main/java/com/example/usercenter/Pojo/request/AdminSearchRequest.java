package com.example.usercenter.Pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author CloudyW
 * @version 1.0
 */
@Data
public class AdminSearchRequest implements Serializable {

    private static final long serialVersionUID = 1846356633189734194L;

    private String userName;
    private String userAccount;
    private Integer gender;
    private String phone;
    private String email;
}
