package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.UserCoin;
import com.foxmo.bilibili.mapper.UserCoinMapper;
import com.foxmo.bilibili.service.UserCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCoinServiceImpl implements UserCoinService {
    @Autowired
    private UserCoinMapper userCoinMapper;

    @Override
    public UserCoin getUserCoinByUserId(Long userId) {
        return userCoinMapper.selectUserCionByUserId(userId);
    }

    @Override
    public void addUserCoin(UserCoin userCoin) {
        userCoinMapper.insertUserCoin(userCoin);
    }

    @Override
    public void modifyUserCoin(UserCoin dbUserCoin) {
        userCoinMapper.updateUserCoin(dbUserCoin);
    }
}
