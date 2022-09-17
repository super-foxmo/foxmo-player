package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.UserFollowing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFollowingMapper {

    void deleteUserFollowingByUIdAndFId(@Param("userId") Long userId, @Param("followingId") Long followingId);

    void insertUserFollowing(UserFollowing userFollowing);

    List<UserFollowing> selectUserFollowingsByUserId(Long userId);

    List<UserFollowing> selectUserFollowingsByFollowingId(Long userId);
}
