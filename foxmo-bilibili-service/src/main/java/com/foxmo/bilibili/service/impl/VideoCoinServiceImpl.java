package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.VideoCoin;
import com.foxmo.bilibili.mapper.VideoCoinMapper;
import com.foxmo.bilibili.service.VideoCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoCoinServiceImpl implements VideoCoinService {
    @Autowired
    private VideoCoinMapper videoCoinMapper;

    @Override
    public VideoCoin getVideoCoinByUserIdAndVideoId(Long userId, Long videoId) {
        return videoCoinMapper.selectVideoCoinByUserIdAndVideoId(userId,videoId);
    }

    @Override
    public void addVideoCoin(VideoCoin videoCoin) {
        videoCoinMapper.insertVideoCoin(videoCoin);
    }

    @Override
    public void modifyVideoCoin(VideoCoin videoCoin) {
        videoCoinMapper.updateVideoCoin(videoCoin);
    }

    @Override
    public Long getVideoCoinCountByVideoId(Long videoId) {
        return videoCoinMapper.selectVideoCoinCountByVideoId(videoId);
    }
}
