package com.example.usercenter.Pojo.request;

import lombok.Data;
import java.io.Serializable;

/**
 * @author CloudyW
 * @version 1.0
 */
@Data
public class UserUpdateInfoRequest implements Serializable {

    private static final long serialVersionUID = -5800707575492356608L;

    private String username;
    private String userAccount;
    private Integer gender;
    private String phone;
    private String email;
    private String avatar;
}
