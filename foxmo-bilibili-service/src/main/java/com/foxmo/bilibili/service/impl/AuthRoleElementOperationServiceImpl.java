package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.auth.AuthRoleElementOperation;
import com.foxmo.bilibili.mapper.AuthRoleElementOperationMapper;
import com.foxmo.bilibili.service.AuthRoleElementOperationService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleElementOperationServiceImpl implements AuthRoleElementOperationService {

    @Autowired
    private AuthRoleElementOperationMapper authRoleElementOperationMapper;

    @Override
    public List<AuthRoleElementOperation> getAuthRoleElementOperationList(Set<Long> roleIdSet) {
        return authRoleElementOperationMapper.selectAuthRoleElementOperationList(roleIdSet);
    }
}
