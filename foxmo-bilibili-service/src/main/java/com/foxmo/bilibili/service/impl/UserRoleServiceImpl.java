package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.auth.UserRole;
import com.foxmo.bilibili.mapper.UserRoleMapper;
import com.foxmo.bilibili.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<UserRole> getUserRoleByUserId(Long userId) {
        List<UserRole> userRoleList = userRoleMapper.selectUserRoleByUserId(userId);
        return userRoleList;
    }

    @Override
    public void addUserRole(UserRole userRole) {
        userRoleMapper.insertUserRole(userRole);
    }
}
