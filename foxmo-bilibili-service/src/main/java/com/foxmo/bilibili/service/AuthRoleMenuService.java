package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.auth.AuthRoleMenu;

import java.util.List;
import java.util.Set;

public interface AuthRoleMenuService {
    List<AuthRoleMenu> getAuthRoleMenuList(Set<Long> roleIdSet);
}
