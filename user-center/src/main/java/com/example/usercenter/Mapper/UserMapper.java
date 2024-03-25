package com.example.usercenter.Mapper;

import com.example.usercenter.Pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86151
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-03-03 16:13:54
* @Entity com.example.usercenter.Pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




