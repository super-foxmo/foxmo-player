package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.VideoCoin;

public interface VideoCoinService {
    VideoCoin getVideoCoinByUserIdAndVideoId(Long userId, Long videoId);

    void addVideoCoin(VideoCoin videoCoin);

    void modifyVideoCoin(VideoCoin videoCoin);

    Long getVideoCoinCountByVideoId(Long videoId);
}
