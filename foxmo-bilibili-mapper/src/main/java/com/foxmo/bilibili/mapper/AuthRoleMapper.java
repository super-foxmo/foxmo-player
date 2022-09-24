package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.auth.AuthRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRoleMapper {

    AuthRole selectAuthRoleByCode(String defaultCode);
}
