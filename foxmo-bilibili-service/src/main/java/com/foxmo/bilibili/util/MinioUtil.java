package com.foxmo.bilibili.util;

import cn.hutool.core.date.DateUtil;
import com.foxmo.bilibili.config.properties.MinioProperties;
import com.foxmo.bilibili.domain.exception.ConditionException;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Random;


@Slf4j
@Component
public class MinioUtil {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    private final Long maxSize = (long) (1024 * 1024);

    //获取文件类型
    public String getFileType(MultipartFile file){

        if(file == null){
            throw new ConditionException("非法文件！");
        }
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        String type = fileName.substring(index + 1);
        return type;
    }

    //MultipartFile --> File
    public File multipartFileToFile(MultipartFile multipartFile) throws Exception{
        String originalFilename = multipartFile.getOriginalFilename();
        String[] fileName = originalFilename.split("\\.");
        //fileName[0] : 文件名
        //fileName[1] ：文件类型
        //两者之间缺少一个点
        File file = File.createTempFile(fileName[0], "." + fileName[1]);
        multipartFile.transferTo(file);
//        File file = new File(multipartFile.getOriginalFilename());
//        multipartFile.transferTo(file);
        return file;
    }


//    public String uploadFile(MultipartFile multipartFile,String bucketName) throws Exception{
//        File file = this.multipartFileToFile(multipartFile);
//
//        String name = multipartFile.getName();
//        String originalFilename = multipartFile.getOriginalFilename();
//
//
//        String filePath = file.getAbsolutePath();
//        String fileName = file.getName();
//        log.info(filePath);
//        log.info(fileName);
//        try {
//            //查询minio中是否存在该桶
//            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
//            //若不存在，则新建该桶
//            if (!found) {
//                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
//            }
//
//            minioClient.uploadObject(
//                    UploadObjectArgs.builder()
//                            .bucket(bucketName)
//                            .object(fileName)   //文件上传后的文件名
//                            .filename(filePath)     //文件的本地绝对路径
//                            .build());
//
//        } catch (Exception e) {
//            System.out.println("Error occurred: " + e);
//        }
//
//        return null;
//    }

    /**
     * 上传文件
     */
    public String uploadFile(MultipartFile file, String bucketName) throws Exception {
        //判断文件是否为空
        if (null == file || 0 == file.getSize()) {
            return null;
        }
        //判断存储桶是否存在  不存在则创建
        createBucket(bucketName);
        //文件名
        String fileName = file.getOriginalFilename();

        //开始上传

        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                                file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
        String url = minioProperties.getEndpoint() + "/" + bucketName + "/" + fileName;
        return url;
    }

    /**
     * 创建bucket
     */
    public void createBucket(String bucketName) throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }


}
