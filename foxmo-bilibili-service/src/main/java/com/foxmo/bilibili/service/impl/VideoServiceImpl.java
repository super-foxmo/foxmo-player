package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.*;
import com.foxmo.bilibili.domain.exception.ConditionException;
import com.foxmo.bilibili.mapper.VideoMapper;
import com.foxmo.bilibili.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserCoinServiceImpl userCoinService;

    @Autowired
    private VideoCoinServiceImpl videoCoinService;

    @Autowired
    private VideoCommentServiceImpl videoCommentService;

    @Override
    @Transactional
    public void addVideo(Video video) {
        Date date = new Date();
        video.setCreateTime(date);
        videoMapper.insertVideo(video);

        Long videoId = video.getId();
        List<VideoTag> videoTagList = video.getVideoTagList();
        videoTagList.forEach(item -> {
            item.setVideoId(videoId);
            item.setCreateTime(date);
        });

        videoMapper.batchAddVideoTags(videoTagList);
    }

    /**
     * 分页查询
     * @param size   每页条数
     * @param no    当前页数
     * @param area  分区
     * @return
     */
    @Override
    public PageResult<Video> pageListVideos(Integer size, Integer no, String area) {
        if (size == null || no == null){
            throw new ConditionException("参数异常！");
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("start",(no - 1) * size);
        params.put("size",size);
        params.put("area",area);
        List<Video> videoList = new ArrayList<>();
        //获取满足条件的所有视频总数
        Integer total = videoMapper.pageCountVideos(params);
        if(total > 0){
            videoList = videoMapper.pageListVideos(params);
        }
        return new PageResult<>(total,videoList);
    }

    /**
     * 添加视频点赞记录
     * @param videoId   视频ID
     * @param userId    用户ID
     */
    @Override
    public void addVideoLike(Long videoId, Long userId) {
        Video video = videoMapper.selectVideoById(videoId);
        if (video == null){
            throw new ConditionException("非法视频！");
        }
        VideoLike videoLike = videoMapper.selectVideoLikeByVideoIdAndUserId(videoId,userId);
        if (videoLike != null){
            throw new ConditionException("视频已点赞！");
        }
        videoLike = new VideoLike();
        videoLike.setVideoId(videoId);
        videoLike.setUserId(userId);
        videoLike.setCreateTime(new Date());

        videoMapper.insertVideoLike(videoLike);
    }

    /**
     * 取消视频点赞
     * @param videoId   视频ID
     * @param userId    用户ID
     */
    @Override
    public void deleteVideoLike(Long userId, Long videoId) {
        videoMapper.deleteVideoLike(userId,videoId);
    }

    /**
     * 查询视频点赞数量和是否已点赞
     * @param userId    用户ID
     * @param videoId   视频ID
     * @return
     */
    @Override
    public Map<String, Object> getVideoLikeInfo(Long userId,Long videoId) {
        Long count = videoMapper.selectVideoLikeCountByVideoId(videoId);
        VideoLike videoLike = videoMapper.selectVideoLikeByVideoIdAndUserId(videoId, userId);
        Boolean like = videoLike != null;
        HashMap<String, Object> result = new HashMap<>();
        result.put("count",count);
        result.put("like",like);

        return result;
    }

    /**
     * 新增视频收藏
     * @param videoCollection
     */
    @Override
    @Transactional
    public void addVideoCollection(VideoCollection videoCollection) {
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        Long userId = videoCollection.getUserId();
        if (videoId == null || groupId == null){
            throw new ConditionException("参数异常！");
        }
        Video video = videoMapper.selectVideoById(videoId);
        if (video == null){
            throw new ConditionException("非法视频！");
        }

        //删除原有视频收藏
        videoMapper.deleteVideoCollection(videoId,userId);
        //添加新的视频收藏
        videoCollection.setCreateTime(new Date());
        videoMapper.insertVideoCollection(videoCollection);
    }

    /**
     * 取消视频收藏
     * @param videoId   视频ID
     * @param userId    用户ID
     */
    @Override
    public void deleteVideoCollection(Long videoId, Long userId) {
        videoMapper.deleteVideoCollection(videoId,userId);
    }

    /**
     * 查询视频的收藏数量和是否已收藏
     * @param videoId   视频ID
     * @param userId    用户ID
     * @return
     */
    @Override
    public Map<String, Object> getVideoCollectionInfo(Long videoId, Long userId) {
        Long count = videoMapper.selectVideoCollectionCount(videoId);
        VideoCollection videoCollection = videoMapper.selectVideoCollectionByVideoIdAndUserId(videoId,userId);

        Boolean collection = videoCollection != null;
        HashMap<String, Object> result = new HashMap<>();
        result.put("count",count);
        result.put("collection",collection);

        return result;
    }

    /**
     * 视频打赏
     * @param videoCoin
     */
    @Override
    @Transactional
    public void addVideoCoin(VideoCoin videoCoin) {
        Long videoId = videoCoin.getVideoId();
        Long amount = videoCoin.getAmount();
        Long userId = videoCoin.getUserId();
        if (videoId == null || amount == null){
            throw new ConditionException("参数异常！");
        }
        //判断视频是否合法
        Video video = videoMapper.selectVideoById(videoId);
        if (video == null){
            throw new ConditionException("非法视频！");
        }

        //查询当前登录用户是否拥有足够的硬币
        UserCoin dbUserCoin = userCoinService.getUserCoinByUserId(userId);
        //用户当前拥有的硬币数量
        Long userCoinAmount = dbUserCoin.getAmount();
        userCoinAmount = userCoinAmount == null ? 0 : userCoinAmount;
        if (amount > userCoinAmount){
            throw new ConditionException("当前硬币数量不足！");
        }
        //查询当前登录用户已对该视频打赏硬币的数量
        VideoCoin dbVideoCoin = videoCoinService.getVideoCoinByUserIdAndVideoId(userId,videoId);

        if (dbVideoCoin == null){   //当前登录用户未对该视频投过币
            //新增视频打赏
            videoCoin.setCreateTime(new Date());
            videoCoinService.addVideoCoin(videoCoin);
        }else{  //当前登录用户已对该视频投过币
            //用户已打赏该视频的硬币数量
            Long dbAmount = dbVideoCoin.getAmount();
            //修改视频打赏信息
            videoCoin.setAmount(dbAmount + amount);
            videoCoin.setUpdateTime(new Date());
            videoCoinService.modifyVideoCoin(videoCoin);
        }
        //更新用户当前硬币总数
        dbUserCoin.setAmount(userCoinAmount - amount);
        dbUserCoin.setUpdateTime(new Date());
        userCoinService.modifyUserCoin(dbUserCoin);
    }

    @Override
    public Map<String, Object> getVideoCoinInfo(Long userId, Long videoId) {
        //查询该视频打赏硬币的总数
        Long count = videoCoinService.getVideoCoinCountByVideoId(videoId);
        VideoCoin videoCoin = videoCoinService.getVideoCoinByUserIdAndVideoId(userId, videoId);
        //当前用户是否打赏过该视频
        Boolean like = videoCoin != null;
        HashMap<String, Object> result = new HashMap<>();
        result.put("count",count);
        result.put("like",like);

        return result;
    }


    /**
     * 添加视频评论
     * @param videoComment    视频评论
     */
    @Override
    public void addVideoComment(VideoComment videoComment) {
        Long videoId = videoComment.getVideoId();
        if(videoId == null){
            throw new ConditionException("参数异常！");
        }
        Video video = videoMapper.selectVideoById(videoId);
        if(video == null){
            throw new ConditionException("非法视频！");
        }

        videoComment.setCreateTime(new Date());
        videoCommentService.addVideoComment(videoComment);
    }

    /**
     * 分页查询视频的评论信息 ※※※※※※※※※※※
     * @param size  每页总条数
     * @param no    当前页数
     * @param videoId   查询视频ID
     * @return
     */
    @Override
    public PageResult<VideoComment> pageListVideoComments(Integer size, Integer no, Long videoId) {
        Video video = videoMapper.selectVideoById(videoId);
        if(video == null){
            throw new ConditionException("非法视频！");
        }
        //封装查询条件
        Map<String, Object> params = new HashMap<>();
        params.put("start", (no-1)*size);
        params.put("limit", size);
        params.put("videoId", videoId);
        //查询视频相关一级评论的总条数
        Integer total = videoCommentService.pageCountVideoTopComments(params);
        List<VideoComment> list = new ArrayList<>();
        if(total > 0){
            //查询视频相关一级评论
            list = videoCommentService.pageListVideoTopComments(params);
            //获取一级评论的ID集合
            List<Long> parentIdList = list.stream().map(VideoComment::getId).collect(Collectors.toList());
            //批量查询二级评论
            List<VideoComment> childCommentList = videoCommentService.batchGetVideoCommentsByRootIds(parentIdList);
            //批量查询用户信息(set集合可以去重)
            Set<Long> userIdList = list.stream().map(VideoComment::getUserId).collect(Collectors.toSet());
            Set<Long> replyUserIdList = childCommentList.stream().map(VideoComment::getUserId).collect(Collectors.toSet());
            Set<Long> childUserIdList = childCommentList.stream().map(VideoComment::getReplyUserId).collect(Collectors.toSet());

            userIdList.addAll(replyUserIdList);
            userIdList.addAll(childUserIdList);
            //批量查询用户详细信息
            List<UserInfo> userInfoList = userService.batchGetUserInfoByUserIds(userIdList);
            Map<Long, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo :: getUserId, userInfo -> userInfo));
            list.forEach(comment -> {
                Long id = comment.getId();
                List<VideoComment> childList = new ArrayList<>();
                childCommentList.forEach(child -> {
                    if(id.equals(child.getRootId())){
                        child.setUserInfo(userInfoMap.get(child.getUserId()));
                        child.setReplyUserInfo(userInfoMap.get(child.getReplyUserId()));
                        childList.add(child);
                    }
                });
                comment.setChildList(childList);
                comment.setUserInfo(userInfoMap.get(comment.getUserId()));
            });
        }
        return new PageResult<>(total, list);
    }

    /**
     * 获取视频详情
     * @param videoId   视频ID
     * @return
     */
    @Override
    public Map<String, Object> getVideoDetail(Long videoId) {
        Video video = videoMapper.selectVideoById(videoId);
        Long userId = video.getUserId();
        User user = userService.getUserInfoByUserId(userId);
        UserInfo userInfo = user.getUserInfo();

        Map<String, Object> result = new HashMap<>();
        result.put("video",video);
        result.put("userInfo",userInfo);
        return result;
    }
}
