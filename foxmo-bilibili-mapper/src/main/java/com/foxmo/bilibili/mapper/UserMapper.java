package com.foxmo.bilibili.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select name from user where id = #{id}")
    public String selectNameById(Integer id);
}
