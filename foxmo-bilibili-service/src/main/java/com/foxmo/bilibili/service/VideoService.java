package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.*;

import java.util.Map;

public interface VideoService {
    void addVideo(Video video);

    PageResult<Video> pageListVideos(Integer size, Integer no, String area);

    void addVideoLike(Long videoId, Long userId);

    void deleteVideoLike(Long userId, Long videoId);

    Map<String, Object> getVideoLikeInfo(Long userId,Long videoId);

    void addVideoCollection(VideoCollection videoCollection);

    void deleteVideoCollection(Long videoId, Long userId);

    Map<String, Object> getVideoCollectionInfo(Long videoId, Long userId);

    void addVideoCoin(VideoCoin videoCoin);

    Map<String, Object> getVideoCoinInfo(Long userId, Long videoId);

    void addVideoComment(VideoComment videoComment);

    PageResult<VideoComment> pageListVideoComments(Integer size, Integer no, Long videoId);

    Map<String, Object> getVideoDetail(Long videoId);
}
