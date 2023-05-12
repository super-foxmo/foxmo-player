package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.VideoComment;
import com.foxmo.bilibili.mapper.VideoCommentMapper;
import com.foxmo.bilibili.service.VideoCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VideoCommentServiceImpl implements VideoCommentService {
    @Autowired
    private VideoCommentMapper videoCommentMapper;

    /**
     *  添加视频评论
     */
    @Override
    public void addVideoComment(VideoComment videoComment) {
        videoCommentMapper.insertVideoComment(videoComment);
    }

    /**
     *  查询视频相关一级评论的总条数
     * @param params    条件参数集合
     * @return
     */
    @Override
    public Integer pageCountVideoTopComments(Map<String, Object> params) {
        return videoCommentMapper.pageCountVideoTopComments(params);
    }

    /**
     * 查询视频相关一级评论
     * @param params    条件参数集合
     * @return
     */
    @Override
    public List<VideoComment> pageListVideoTopComments(Map<String, Object> params) {
        return videoCommentMapper.pageListVideoTopComments(params);
    }

    /**
     * 批量查询二级评论
     * @param parentIdList  一级评论的ID集合
     * @return
     */
    @Override
    public List<VideoComment> batchGetVideoCommentsByRootIds(List<Long> parentIdList) {
        return videoCommentMapper.batchGetVideoCommentsByRootIds(parentIdList);
    }
}
