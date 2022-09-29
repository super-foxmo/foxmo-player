package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.FollowingGroup;

import java.util.List;

public interface FollowingGroupService {
    FollowingGroup getFollowingGroupById(Long id);

    FollowingGroup getFollowingGroupByType(String type);
    //获取所有关注分组（包含系统分组）
    List<FollowingGroup> getFollowingGroups(Long userId);

    void insertFollowingGroup(FollowingGroup followingGroup);
    //获取所有自定义分组
    List<FollowingGroup> getFollowingGroupsByUserId(Long userId);
}
