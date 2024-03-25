package com.example.usercenter.Service;
import java.util.Date;

import com.example.usercenter.Pojo.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("test01");
        user.setUserAccount("123");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("123");
        user.setEmail("456");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);
    }

    @Test
    void userRegister() {
        String userName = "tset";
        String userAccount = "1234";
        String userPassword = "123456";
        String checkPassword = "123456";
        long result = userService.userRegister(userName, userAccount, userPassword, checkPassword, 1);
        assertEquals(-1, result);

//        userAccount = "cloudyW";
//        userPassword = "123";
//        checkPassword = "123";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        assertEquals(-1, result);

//        userAccount = "test";
//        userPassword = "123456";
//        checkPassword = "123456";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        assertEquals(-1, result);

//        userAccount = "cloudyW";
//        userPassword = "123458";
//        checkPassword = "123456";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        assertEquals(-1, result);
//
//        userAccount = "cloudyW";
//        userPassword = "123456";
//        checkPassword = "123456";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        assertTrue(result > 0);
    }

    @Test
    void resetPassword() {
        userService.resetPassword();
    }
}