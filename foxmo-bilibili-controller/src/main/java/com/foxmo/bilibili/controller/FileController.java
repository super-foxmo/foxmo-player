package com.foxmo.bilibili.controller;

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
    private FileServiceImpl fileService;

    @Autowired
    private MinioUtil minioUtil;

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
        String fileUrl = fileService.uploadFile(multipartFile, bucketName);
        return new JsonResponse<>(fileUrl);
    }

    //Minio 文件删除
    @DeleteMapping("/minio-files")
    public JsonResponse<String> DeleteFile(String bucketName,String fileName) throws Exception{
        fileService.deleteFile(bucketName,fileName);

        return JsonResponse.success();
    }

    //文件下载
    @GetMapping("/minio-files")
    public JsonResponse<String> downloadFile(String bucketName,String fileName) throws Exception{
        fileService.downloadFile(bucketName,fileName);

        return JsonResponse.success();
    }

    @GetMapping("/test")
    public JsonResponse<String> getInfo(MultipartFile file) throws Exception{
//        List<Bucket> allBuckets = minioUtil.getAllBuckets();
//        minioUtil.createBucket("file");
//        Optional<Bucket> bucket = minioUtil.getBucket("video");
//        //String objectURL = minioUtil.getObjectURL("video", "通信202-李晓辉-综测佐证.docx", 2);
//
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("allBuckets",allBuckets);
//        map.put("videoBucket",bucket);
//       //map.put("objectURL",objectURL);

//        minioUtil.removeObject("video","db_reggie.sql");
//        minioUtil.removeObject("video","仿bilibili/imooc-bilibili-eureka-master.zip");
//        minioUtil.removeBucket("file");

//        StatObjectResponse objectInfo = minioUtil.getObjectInfo("video", "通信202-李晓d辉-综测佐证.docx");

//        InputStream inputStream;
//        try {
//             inputStream = minioUtil.getObject("video", "通信202-李w晓辉-综测佐证.docx");
//
//        }catch (Exception e){
//            throw new ConditionException("服务器中不存在该文件！");
//        }
//        MultipartFile multipartFile = minioUtil.getMultipartFile(inputStream);
//        String fileMD5 = MD5Util.getFileMD5(multipartFile);

        return JsonResponse.success();
    }
}
