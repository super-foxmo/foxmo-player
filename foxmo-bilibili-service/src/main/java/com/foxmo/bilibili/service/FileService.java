package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    // fastDFS
    //String uploadFileBySlices(MultipartFile slice, String fileMd5, Integer sliceNo, Integer totalSliceNo) throws Exception;

//    String uploadFile();

    String getFileMD5(MultipartFile file) throws Exception;

    String uploadFile(MultipartFile multipartFile, String bucketName,Long userId) throws Exception;

    void deleteFile(File file) throws Exception;

    void downloadFile(File file) throws Exception;
}
