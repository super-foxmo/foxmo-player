package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.FollowingGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowingGroupMapper {

    FollowingGroup selectFollowingGroupById(Long id);

    FollowingGroup selectFollowingGroupByType(String type);

    List<FollowingGroup> selectFollowingGroups(Long userId);

    Long insertFollowingGroup(FollowingGroup followingGroup);

    List<FollowingGroup> selectFollowingGroupsByUserId(Long userId);
}
