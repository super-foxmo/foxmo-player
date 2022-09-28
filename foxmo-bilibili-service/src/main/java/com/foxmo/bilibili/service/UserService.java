package com.foxmo.bilibili.service;

import com.alibaba.fastjson.JSONObject;
import com.foxmo.bilibili.domain.PageResult;
import com.foxmo.bilibili.domain.User;
import com.foxmo.bilibili.domain.UserInfo;

import java.util.List;
import java.util.Map;
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
    //批量查询用户详细信息
    List<UserInfo> getUserInfosByIds(Set<Long> followingIdSet);

    PageResult<UserInfo> pageListUserInfo(JSONObject param);
    //双 token 身份用户登录
    Map<String,Object> loginForDts(User user) throws Exception;

    Integer deleteRefreshToken(String refreshToken, Long userId);
    //刷新token
    String refreshAccessToken(String refreshToken) throws Exception;
    //批量查询用户详细信息
    List<UserInfo> batchGetUserInfoByUserIds(Set<Long> userIdList);
    //查询用户详细信息
    User getUserInfoByUserId(Long userId);
}
