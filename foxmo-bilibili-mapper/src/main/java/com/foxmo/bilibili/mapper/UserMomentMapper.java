package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMomentMapper {
    void insertUserMoment(UserMoment userMoment);
}
