package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.Video;
import com.foxmo.bilibili.domain.VideoCollection;
import com.foxmo.bilibili.domain.VideoLike;
import com.foxmo.bilibili.domain.VideoTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface VideoMapper {
    Integer insertVideo(Video video);

    Integer batchAddVideoTags(@Param("videoTagList") List<VideoTag> videoTagList);

    //获取满足条件的视频总条数
    Integer pageCountVideos(HashMap<String, Object> params);
    //分页查询视频
    List<Video> pageListVideos(HashMap<String, Object> params);

    Video selectVideoById(@Param("id") Long videoId);

    VideoLike selectVideoLikeByVideoIdAndUserId(@Param("videoId") Long videoId, @Param("userId") Long userId);

    void insertVideoLike(VideoLike videoLike);

    void deleteVideoLike(@Param("userId") Long userId,@Param("videoId") Long videoId);

    Long selectVideoLikeCountByVideoId(Long videoId);

    void deleteVideoCollection(@Param("videoId") Long videoId, @Param("userId") Long userId);

    void insertVideoCollection(VideoCollection videoCollection);

    Long selectVideoCollectionCount(Long videoId);

    VideoCollection selectVideoCollectionByVideoIdAndUserId(@Param("videoId") Long videoId, @Param("userId") Long userId);
}
