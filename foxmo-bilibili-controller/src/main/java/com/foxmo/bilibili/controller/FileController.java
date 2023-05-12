package com.foxmo.bilibili.controller;

import com.foxmo.bilibili.controller.support.UserSupport;
import com.foxmo.bilibili.domain.File;
import com.foxmo.bilibili.domain.JsonResponse;
import com.foxmo.bilibili.domain.exception.ConditionException;
import com.foxmo.bilibili.service.impl.FileServiceImpl;
import com.foxmo.bilibili.util.FastDFSUtil;
import com.foxmo.bilibili.util.MD5Util;
import com.foxmo.bilibili.util.MinioUtil;
import io.minio.StatObjectResponse;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@RestController
public class FileController {
    @Autowired
    private UserSupport userSupport;

    @Autowired
    private FileServiceImpl fileService;

    @PostMapping("/file-md5")
    public JsonResponse<String> getFileMD5(MultipartFile file) throws Exception {
        String fileMD5 = fileService.getFileMD5(file);
        return new JsonResponse<>(fileMD5);
    }


    /* fastDFS 分片文件上传
    @PutMapping("/file-slices")
    public JsonResponse<String> uploadFileBySlices(MultipartFile slice,
                                                   String fileMd5,
                                                   Integer sliceNo,
                                                   Integer totalSliceNo) throws Exception {
        String filePath = fileService.uploadFileBySlices(slice, fileMd5, sliceNo, totalSliceNo);
        return new JsonResponse<>(filePath);
    }
    */

    //Minio 文件上传
    @PostMapping("/minio-files")
    public JsonResponse<String> uploadFile(MultipartFile multipartFile,String bucketName) throws Exception{
        String fileUrl = fileService.uploadFile(multipartFile, bucketName,userSupport.getCurrentUserId());
        return new JsonResponse<>(fileUrl);
    }

    //Minio 文件删除
    @DeleteMapping("/minio-files")
    public JsonResponse<String> DeleteFile(@RequestBody File file) throws Exception{
        file.setUserId(userSupport.getCurrentUserId());
        fileService.deleteFile(file);
        return JsonResponse.success();
    }

    //文件下载
    @GetMapping("/minio-files")
    public JsonResponse<String> downloadFile(@RequestBody File file) throws Exception{

        fileService.downloadFile(file);

        return JsonResponse.success();
    }
}
