package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.auth.AuthRoleMenu;
import com.foxmo.bilibili.mapper.AuthRoleMenuMapper;
import com.foxmo.bilibili.service.AuthRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleMenuServiceImpl implements AuthRoleMenuService {

    @Autowired
    private AuthRoleMenuMapper authRoleMenuMapper;

    @Override
    public List<AuthRoleMenu> getAuthRoleMenuList(Set<Long> roleIdSet) {
        return authRoleMenuMapper.selectAuthRoleMenuList(roleIdSet);
    }
}
