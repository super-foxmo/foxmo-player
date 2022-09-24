package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.Video;
import com.foxmo.bilibili.domain.VideoTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface VideoMapper {
    Integer insertVideo(Video video);

    Integer batchAddVideoTags(@Param("videoTagList") List<VideoTag> videoTagList);

    Integer pageCountVideos(HashMap<String, Object> params);

    List<Video> pageListVideos(HashMap<String, Object> params);
}
