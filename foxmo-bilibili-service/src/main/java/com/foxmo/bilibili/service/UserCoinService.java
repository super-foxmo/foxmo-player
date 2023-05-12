package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.UserCoin;

public interface UserCoinService {
    UserCoin getUserCoinByUserId(Long userId);

    void addUserCoin(UserCoin userCoin);

    void modifyUserCoin(UserCoin dbUserCoin);
}
