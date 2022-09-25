package com.foxmo.bilibili.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    // fastDFS
    //String uploadFileBySlices(MultipartFile slice, String fileMd5, Integer sliceNo, Integer totalSliceNo) throws Exception;

//    String uploadFile();

    String getFileMD5(MultipartFile file) throws Exception;

    String uploadFile(MultipartFile multipartFile, String bucketName) throws Exception;

    void deleteFile(String bucket, String fileName) throws Exception;
}
