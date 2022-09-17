package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.*;
import com.foxmo.bilibili.domain.constant.UserConstant;
import com.foxmo.bilibili.domain.exception.ConditionException;
import com.foxmo.bilibili.mapper.UserFollowingMapper;
import com.foxmo.bilibili.service.UserFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserFollowingServiceImpl implements UserFollowingService {
    @Autowired
    UserFollowingMapper userFollowingMapper;
    @Autowired
    FollowingGroupServiceImpl followingGroupService;
    @Autowired
    UserServiceImpl userService;

    /**
     * 添加用户关注
     * @param userFollowing
     */
    @Transactional  //开启事务处理
    public void addUserFollowing(UserFollowing userFollowing){
        Long groupId = userFollowing.getGroupId();
        if (groupId == null){
            //设置为默认分组
            userFollowing.setGroupId(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
        }else{
            //判断存在 groupId 关注分组
            FollowingGroup followingGroup = followingGroupService.getFollowingGroupById(groupId);
            if (followingGroup == null){
                throw new ConditionException("关注分组不存在！");
            }
        }

        //判断被关注者是否存在
        User user = userService.queryUserById(userFollowing.getFollowingId());
        if (user == null){
            throw new ConditionException("关注的用户不存在！");
        }
        //删除数据库中对应的数据
        userFollowingMapper.deleteUserFollowingByUIdAndFId(userFollowing.getUserId(),userFollowing.getFollowingId());

        userFollowing.setCreateTime(new Date());

        userFollowingMapper.insertUserFollowing(userFollowing);

    }

    /*
     * 第一步：获取关注的用户列表
     * 第二部：根据关注用户的id查询关注用户的基本信息
     * 第三步：将关注用户按关注分组进行分类
     */
    public List<FollowingGroup> getUserFollowingsByUserId(Long userId){
        //获取当前用户所有关注的用户
        List<UserFollowing> userFollowingList = userFollowingMapper.selectUserFollowingsByUserId(userId);
        //获取当前用户所有关注的用户的ID
        Set<Long> followingIdSet = userFollowingList.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        //获取当前用户所有关注的用户的基本信息
        List<UserInfo> userInfoList = new ArrayList<>();
        if (followingIdSet.size() > 0){
            userInfoList = userService.getUserInfosByIds(followingIdSet);
        }
        //将关注的用户与其对应的基本信息关联
        for (UserFollowing userFollowing : userFollowingList) {
            for(UserInfo userInfo : userInfoList){
                if (userFollowing.getFollowingId().equals(userInfo.getUserId())){
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }
        //获取当前用户的所有关注分组
        List<FollowingGroup> groupList = followingGroupService.getFollowingGroupsByUserId(userId);

        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);

        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);
        //将关注用户按关注分组进行分类
        for (FollowingGroup group : groupList){
            List<UserInfo> infoList = new ArrayList<>();
            for (UserFollowing userFollowing : userFollowingList){
                if (group.getType().equals(String.valueOf(userFollowing.getGroupId()))){
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            group.setFollowingUserInfoList(infoList);
            result.add(group);
        }
        return result;
    }

    /*
     * 第一步：获取当前用户的粉丝列表
     * 第二步：根据粉丝的用户id查询基本的信息
     * 第三步：查询当前用户是否已经该粉丝（互相关注）
     */
    public List<UserFollowing> getUsaerFensByUserId(Long userId){
        //获取当前用户的粉丝列表
        List<UserFollowing> fensList = userFollowingMapper.selectUserFollowingsByFollowingId(userId);
        //获取所有粉丝ID
        Set<Long> userIdSet = fensList.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());
        //获取所有粉丝的基本信息
        List<UserInfo> userInfoList = new ArrayList<>();
        if (userIdSet.size() > 0){
            userInfoList = userService.getUserInfosByIds(userIdSet);
        }
        //获取当前用户的所有关注的用户
        List<UserFollowing> userFollowingList = userFollowingMapper.selectUserFollowingsByUserId(userId);
        for (UserFollowing fen : fensList){
            //将粉丝和粉丝的基本信息关联
            for (UserInfo userInfo : userInfoList){
                if (fen.getUserId().equals(userInfo.getUserId())){
                    userInfo.setFollowed(false);
                    fen.setUserInfo(userInfo);
                }
            }
            //判断当前用户和其粉丝之间是否互关，若是 followed：true  /  反之 followed：false
            for (UserFollowing userFollowing : userFollowingList){
                if (userFollowing.getFollowingId().equals(fen.getUserId())){
                    fen.getUserInfo().setFollowed(true);
                }
            }
        }

        return fensList;
    }
}
