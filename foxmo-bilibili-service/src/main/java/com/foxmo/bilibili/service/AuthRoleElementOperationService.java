package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.auth.AuthRoleElementOperation;

import java.util.List;
import java.util.Set;

public interface AuthRoleElementOperationService {
    List<AuthRoleElementOperation> getAuthRoleElementOperationList(Set<Long> roleIdSet);
}
