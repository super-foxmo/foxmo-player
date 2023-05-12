package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.auth.*;
import com.foxmo.bilibili.domain.constant.AuthRoleConstant;
import com.foxmo.bilibili.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private AuthRoleServiceImpl authRoleService;

    @Override
    public UserAuthorities getUserAuthoritiesByUserId(Long userId) {
        //获取当前用户的所有权限列表
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        //获取当前用户锁头权限ID
        Set<Long> roleIdSet = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());

        List<AuthRoleElementOperation> authRoleElementOperationList = authRoleService.getAuthRoleElementOperationList(roleIdSet);
        List<AuthRoleMenu> authRoleMenuList = authRoleService.getAuthRoleMenuList(roleIdSet);

        return new UserAuthorities(authRoleElementOperationList,authRoleMenuList);
    }

    @Override
    public void addUserDefaultRole(Long userId) {
        //新用户默认会员等级为 Lv0
        AuthRole authRole = authRoleService.getAuthRoleByCode(AuthRoleConstant.ROLE_LV0);

        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(authRole.getId());
        userRole.setCreateTime(new Date());

        userRoleService.addUserRole(userRole);
    }
}
