package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.VideoComment;

import java.util.List;
import java.util.Map;

public interface VideoCommentService {
    void addVideoComment(VideoComment videoComment);

    Integer pageCountVideoTopComments(Map<String, Object> params);

    List<VideoComment> pageListVideoTopComments(Map<String, Object> params);

    List<VideoComment> batchGetVideoCommentsByRootIds(List<Long> parentIdList);
}
