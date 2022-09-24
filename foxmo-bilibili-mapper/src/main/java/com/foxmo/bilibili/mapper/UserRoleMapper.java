package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.auth.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    List<UserRole> selectUserRoleByUserId(Long userId);

    void insertUserRole(UserRole userRole);
}
