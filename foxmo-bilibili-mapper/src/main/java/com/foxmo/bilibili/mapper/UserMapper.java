package com.foxmo.bilibili.mapper;

import com.alibaba.fastjson.JSONObject;
import com.foxmo.bilibili.domain.RefreshTokenDetail;
import com.foxmo.bilibili.domain.User;
import com.foxmo.bilibili.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;
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

    Integer pageCountUserInfos(Map<String,Object> param);

    List<UserInfo> selectPageListUserInfo(Map<String,Object> param);

    User selectUserByPhoneOrEmail(String phone);

    Integer deleteRefreshToken(@Param("refreshToken") String refreshToken, @Param("userId") Long userId);

    Integer insertRefreshToken(@Param("refreshToken") String refreshToken, @Param("userId") Long userId, @Param("createTime") Date createTime);

    RefreshTokenDetail selectRefreshTokenDetail(String refreshToken);
}
