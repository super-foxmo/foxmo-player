package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.mapper.UserMapper;
import com.foxmo.bilibili.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public String queryNameById(Integer id) {
        return userMapper.selectNameById(id);
    }
}
