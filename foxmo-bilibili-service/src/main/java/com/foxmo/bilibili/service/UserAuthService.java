package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.auth.UserAuthorities;

public interface UserAuthService {
    UserAuthorities getUserAuthoritiesByUserId(Long userId);

    void addUserDefaultRole(Long id);
}
