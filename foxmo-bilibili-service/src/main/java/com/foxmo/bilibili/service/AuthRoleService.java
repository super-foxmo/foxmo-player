package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.auth.AuthRole;
import com.foxmo.bilibili.domain.auth.AuthRoleElementOperation;
import com.foxmo.bilibili.domain.auth.AuthRoleMenu;

import java.util.List;
import java.util.Set;

public interface AuthRoleService {
    List<AuthRoleElementOperation> getAuthRoleElementOperationList(Set<Long> roleIdSet);

    List<AuthRoleMenu> getAuthRoleMenuList(Set<Long> roleIdSet);

    AuthRole getAuthRoleByCode(String defaultCode);
}
