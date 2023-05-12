package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.auth.UserRole;

import java.util.List;

public interface UserRoleService {
    List<UserRole> getUserRoleByUserId(Long userId);

    void addUserRole(UserRole userRole);
}
