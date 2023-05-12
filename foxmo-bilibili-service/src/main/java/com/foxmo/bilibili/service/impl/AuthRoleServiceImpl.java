package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.auth.AuthRole;
import com.foxmo.bilibili.domain.auth.AuthRoleElementOperation;
import com.foxmo.bilibili.domain.auth.AuthRoleMenu;
import com.foxmo.bilibili.mapper.AuthRoleMapper;
import com.foxmo.bilibili.service.AuthRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleServiceImpl implements AuthRoleService {

    @Autowired
    private AuthRoleElementOperationServiceImpl authRoleElementOperationService;

    @Autowired
    private AuthRoleMenuServiceImpl authRoleMenuService;

    @Autowired
    private AuthRoleMapper authRoleMapper;

    @Override
    public List<AuthRoleElementOperation> getAuthRoleElementOperationList(Set<Long> roleIdSet) {
        List<AuthRoleElementOperation> authRoleElementOperationList = authRoleElementOperationService.getAuthRoleElementOperationList(roleIdSet);
        return authRoleElementOperationList;
    }

    @Override
    public List<AuthRoleMenu> getAuthRoleMenuList(Set<Long> roleIdSet) {
        return authRoleMenuService.getAuthRoleMenuList(roleIdSet);
    }

    @Override
    public AuthRole getAuthRoleByCode(String defaultCode) {
        return authRoleMapper.selectAuthRoleByCode(defaultCode);
    }
}
