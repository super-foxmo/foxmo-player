package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.VideoCoin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VideoCoinMapper {
    VideoCoin selectVideoCoinByUserIdAndVideoId(@Param("userId") Long userId,@Param("videoId") Long videoId);

    void insertVideoCoin(VideoCoin videoCoin);

    void updateVideoCoin(VideoCoin videoCoin);

    Long selectVideoCoinCountByVideoId(Long videoId);
}
