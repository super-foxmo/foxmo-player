package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.FollowingGroup;
import com.foxmo.bilibili.domain.UserFollowing;
import com.foxmo.bilibili.domain.UserInfo;

import java.util.List;

public interface UserFollowingService {
    void addUserFollowing(UserFollowing userFollowing);

    List<FollowingGroup> getUserFollowingsByUserId(Long userId);

    List<UserFollowing> getUsaerFensByUserId(Long userId);

    Long addFollowingGroup(FollowingGroup followingGroup);

    List<FollowingGroup> getFollowingGroupsByUserId(Long userId);

    List<UserInfo> checkFollowingStatus(List<UserInfo> userInfoList, Long userId);
}
