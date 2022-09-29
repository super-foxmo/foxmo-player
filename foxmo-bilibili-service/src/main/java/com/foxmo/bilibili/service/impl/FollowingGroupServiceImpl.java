package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.FollowingGroup;
import com.foxmo.bilibili.mapper.FollowingGroupMapper;
import com.foxmo.bilibili.service.FollowingGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowingGroupServiceImpl implements FollowingGroupService {
    @Autowired
    FollowingGroupMapper followingGroupMapper;

    @Override
    public FollowingGroup getFollowingGroupById(Long id) {
        return followingGroupMapper.selectFollowingGroupById(id);
    }

    @Override
    public FollowingGroup getFollowingGroupByType(String type) {
        return followingGroupMapper.selectFollowingGroupByType(type);
    }

    @Override
    public List<FollowingGroup> getFollowingGroupsByUserId(Long userId) {
        return followingGroupMapper.selectFollowingGroupsByUserId(userId);
    }

    @Override
    public List<FollowingGroup> getFollowingGroups(Long userId) {
        return followingGroupMapper.selectFollowingGroups(userId);
    }

    @Override
    public void insertFollowingGroup(FollowingGroup followingGroup) {
        followingGroupMapper.insertFollowingGroup(followingGroup);
    }
}
