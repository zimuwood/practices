package com.example.usercenter.Utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercenter.Mapper.UserMapper;
import com.example.usercenter.Pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * 账号生成工具类
 * @author CloudyW
 * @version 1.0
 */
@Service
public class AccountGenerator {

    @Autowired
    UserMapper userMapper;

    public String generateAccount() {
        LocalDate now = LocalDate.now();
        String yearPart = String.valueOf(now.getYear()).substring(2);
        String dayPart = String.valueOf(now.getDayOfYear());
        if (dayPart.length() < 3) {
            StringBuilder dayPartBuilder = new StringBuilder(dayPart);
            dayPartBuilder.append("0".repeat(3 - dayPart.length()));
            dayPart = new String(dayPartBuilder);
        }
        String account = yearPart + dayPart + randomPartGenerator();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", account);
        while (userMapper.selectOne(queryWrapper) != null) {
            account = yearPart + dayPart + randomPartGenerator();
            queryWrapper.eq("user_account", account);
        }
        return account;
    }

    public String randomPartGenerator() {
        StringBuilder randomPart = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            randomPart.append((int) (Math.random() * 10));
        }
        return new String(randomPart);
    }
}
