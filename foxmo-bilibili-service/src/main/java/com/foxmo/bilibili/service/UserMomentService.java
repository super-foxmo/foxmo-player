package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.UserMoment;

import java.util.List;

public interface UserMomentService {
    void addUserMoment(UserMoment userMoment) throws Exception;

    List<UserMoment> getUserSubscribedMoments(Long userId);
}
