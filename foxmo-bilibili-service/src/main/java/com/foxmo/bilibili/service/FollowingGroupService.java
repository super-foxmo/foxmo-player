package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.FollowingGroup;

import java.util.List;

public interface FollowingGroupService {
    FollowingGroup getFollowingGroupById(Long id);

    FollowingGroup getFollowingGroupByType(String type);

    List<FollowingGroup> getFollowingGroupsByUserId(Long userId);
}
