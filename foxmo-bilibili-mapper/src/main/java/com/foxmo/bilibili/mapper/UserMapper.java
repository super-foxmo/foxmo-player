package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.User;
import com.foxmo.bilibili.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    Integer insertUser(User user);

    User selectUserByPhone(String phone);

    Integer insertUserInfo(UserInfo userInfo);

    User selectUserById(Long userId);

    UserInfo selectUserInfoByUserId(Long userId);
}
