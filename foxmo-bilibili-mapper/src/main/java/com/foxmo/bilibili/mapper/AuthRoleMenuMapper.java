package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.auth.AuthRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AuthRoleMenuMapper {

    List<AuthRoleMenu> selectAuthRoleMenuList(@Param("roleIdSet") Set<Long> roleIdSet);
}
