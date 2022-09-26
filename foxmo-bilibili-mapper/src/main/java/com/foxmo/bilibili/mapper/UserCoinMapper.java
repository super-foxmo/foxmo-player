package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.UserCoin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCoinMapper {
    UserCoin selectUserCionByUserId(Long userId);

    void insertUserCoin(UserCoin userCoin);

    void updateUserCoin(UserCoin dbUserCoin);
}
