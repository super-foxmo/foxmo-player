package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.User;
import com.foxmo.bilibili.domain.UserInfo;

import java.util.List;
import java.util.Set;

public interface UserService {
    //注册新用户
    void addUser(User user);
    //根据手机号码查询指定用户
    User queryUserByPhone(String phone);
    //新增用户信息表
    Integer addUserInfo(UserInfo userInfo);
    //用户登录
    String login(User user) throws Exception;

    User queryUserById(Long userId);

    Integer modifyUser(User user) throws Exception;

    void modifyUserInfo(UserInfo userInfo);

    List<UserInfo> getUserInfosByIds(Set<Long> followingIdSet);
}
