package com.foxmo.bilibili.controller;

import com.foxmo.bilibili.controller.support.UserSupport;
import com.foxmo.bilibili.domain.JsonResponse;
import com.foxmo.bilibili.domain.PageResult;
import com.foxmo.bilibili.domain.Video;
import com.foxmo.bilibili.service.VideoService;
import com.foxmo.bilibili.service.impl.VideoServiceImpl;
import com.foxmo.bilibili.util.MD5Util;
import com.foxmo.bilibili.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.lang.management.MemoryUsage;

@RestController
public class VideoController {
    @Autowired
    private UserSupport userSupport;

    @Autowired
    private VideoServiceImpl videoService;

    @Autowired
    private MinioUtil minioUtil;

    @PostMapping("/videos")
    public JsonResponse<String> addVideo(@RequestBody Video video){
        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideo(video);

        return JsonResponse.success();
    }

    @GetMapping("/videos")
    public JsonResponse<PageResult<Video>> pageListVideos(Integer size,Integer no,String area){
        PageResult<Video> result =  videoService.pageListVideos(size,no,area);

        return new JsonResponse<>(result);
    }

    @PostMapping("/minio-videos")
    public JsonResponse<String> uploadFile(MultipartFile multipartFile,String bucketName) throws Exception{
        String fileUrl = minioUtil.uploadFile(multipartFile, bucketName);
        return new JsonResponse<>(fileUrl);
    }

    @GetMapping("/file-md5")
    public JsonResponse<String> getFileMD5(MultipartFile file) throws Exception{
        String fileMD5 = MD5Util.getFileMD5(file);
        return new JsonResponse<>(fileMD5);
    }
}
