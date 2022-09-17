package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.User;
import com.foxmo.bilibili.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {

    Integer insertUser(User user);

    User selectUserByPhone(String phone);

    Integer insertUserInfo(UserInfo userInfo);

    User selectUserById(Long userId);

    UserInfo selectUserInfoByUserId(Long userId);

    Integer updateUser(User user);

    void updateUserInfo(UserInfo userInfo);

    List<UserInfo> selectUserInfosByIds(Set<Long> followingIdSet);
}
