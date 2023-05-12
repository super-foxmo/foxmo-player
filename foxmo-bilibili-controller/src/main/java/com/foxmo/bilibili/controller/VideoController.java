package com.foxmo.bilibili.controller;

import com.foxmo.bilibili.controller.support.UserSupport;
import com.foxmo.bilibili.domain.*;
import com.foxmo.bilibili.service.impl.VideoServiceImpl;
import com.foxmo.bilibili.util.MD5Util;
import com.foxmo.bilibili.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class VideoController {
    @Autowired
    private UserSupport userSupport;

    @Autowired
    private VideoServiceImpl videoService;

    //视频上传
    @PostMapping("/videos")
    public JsonResponse<String> addVideo(@RequestBody Video video){
        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideo(video);

        return JsonResponse.success();
    }

    //视频分页查询
    @GetMapping("/videos")
    public JsonResponse<PageResult<Video>> pageListVideos(Integer size,Integer no,String area){
        PageResult<Video> result =  videoService.pageListVideos(size,no,area);

        return new JsonResponse<>(result);
    }

    //视频点赞
    @PostMapping("/video-likes")
    public JsonResponse<String> addVideoLike(Long videoId){
        Long userId = userSupport.getCurrentUserId();

        videoService.addVideoLike(videoId,userId);
        return JsonResponse.success();
    }

    //取消视频点赞
    @DeleteMapping("/video-likes")
    public JsonResponse<String> deleteVideoLike(Long videoId){
        Long userId = userSupport.getCurrentUserId();

        videoService.deleteVideoLike(userId,videoId);

        return JsonResponse.success();
    }

    //查询视频的点赞数量和是否已点赞
    @GetMapping("/video-likes")
    public JsonResponse<Map<String,Object>> getVideoLikeInfo(Long videoId){
        Long userId = null;
        try{
            userId = userSupport.getCurrentUserId();
        }catch (Exception ignored){}

        Map<String,Object> result = videoService.getVideoLikeInfo(userId,videoId);

        return new JsonResponse<>(result);
    }

    //视频收藏
    @PostMapping("/video-collections")
    public JsonResponse<String> addVideoCollection(@RequestBody VideoCollection videoCollection){
        Long userId = userSupport.getCurrentUserId();
        videoCollection.setUserId(userId);
        videoService.addVideoCollection(videoCollection);

        return JsonResponse.success();
    }

    //取消视频收藏
    @DeleteMapping("/video-collections")
    public JsonResponse<String> deleteVideoCollection(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoCollection(videoId,userId);

        return JsonResponse.success();
    }

    //查询视频的收藏数量和是否已收藏
    @GetMapping("/video-collections")
    public JsonResponse<Map<String,Object>> getVideoCollectionInfo(@RequestParam Long videoId){
        Long userId = null;
        try{
            userId = userSupport.getCurrentUserId();
        }catch (Exception ignored){}

        Map<String,Object> result = videoService.getVideoCollectionInfo(videoId,userId);

        return new JsonResponse<>(result);
    }

    //视频投币
    @PostMapping("/video-coins")
    public JsonResponse<String> addVideoCoin(@RequestBody VideoCoin videoCoin){
        Long userId = userSupport.getCurrentUserId();
        videoCoin.setUserId(userId);
        videoService.addVideoCoin(videoCoin);

        return JsonResponse.success();
    }

    //查询视频的打赏硬币数量和是否已打赏过
    @GetMapping("/video-coins")
    public JsonResponse<Map<String,Object>> getVideoCoinInfo(@RequestParam Long videoId){
        //登录或未登录
        Long userId = null;
        try{
            userId = userSupport.getCurrentUserId();
        }catch (Exception ignored){}

        Map<String,Object> result = videoService.getVideoCoinInfo(userId,videoId);

        return new JsonResponse<>(result);
    }

    //添加视频评论
    @PostMapping("/video-comments")
    public JsonResponse<String> addVideoComment(@RequestBody VideoComment videoComment){
        Long userId = userSupport.getCurrentUserId();
        videoComment.setUserId(userId);
        videoService.addVideoComment(videoComment);

        return JsonResponse.success();
    }

    //分页查询视频评论
    @GetMapping("/videoComments")
    public JsonResponse<PageResult<VideoComment>> pageListVideoComments(@RequestParam Integer size,
                                                                        @RequestParam Integer no,
                                                                        @RequestParam Long videoId){
        PageResult<VideoComment> result = videoService.pageListVideoComments(size, no, videoId);

        return new JsonResponse<>(result);
    }

    //获取视频详情
    @GetMapping("/video-details")
    public JsonResponse<Map<String,Object>> getVideoDetail(@RequestParam Long videoId){
        Map<String,Object> result = videoService.getVideoDetail(videoId);

        return new JsonResponse<>(result);
    }

}
