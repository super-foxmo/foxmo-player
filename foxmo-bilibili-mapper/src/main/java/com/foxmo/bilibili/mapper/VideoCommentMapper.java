package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.VideoComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoCommentMapper {
    void insertVideoComment(VideoComment videoComment);

    Integer pageCountVideoTopComments(Map<String, Object> params);

    List<VideoComment> pageListVideoTopComments(Map<String, Object> params);

    List<VideoComment> batchGetVideoCommentsByRootIds(@Param("parentIdList") List<Long> parentIdList);
}
