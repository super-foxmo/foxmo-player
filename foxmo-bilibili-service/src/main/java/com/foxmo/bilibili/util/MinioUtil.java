package com.foxmo.bilibili.util;

import com.foxmo.bilibili.config.properties.MinioProperties;
import com.foxmo.bilibili.domain.exception.ConditionException;
import io.minio.*;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
public class MinioUtil {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    //获取文件类型
    public String getFileType(MultipartFile file){

        if(file == null){
            throw new ConditionException("非法文件！");
        }
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");

        return fileName.substring(index + 1);
    }

    //MultipartFile --> File
    public File multipartFileToFile(MultipartFile multipartFile) throws Exception{
        String originalFilename = multipartFile.getOriginalFilename();
        String[] fileName = originalFilename.split("\\.");
        File file = File.createTempFile(fileName[0], "." + fileName[1]);
        multipartFile.transferTo(file);
        return file;
    }


//    public String uploadFile(MultipartFile multipartFile,String bucketName) throws Exception{
//        File file = this.multipartFileToFile(multipartFile);
//
//        String name = multipartFile.getName();
//        String originalFilename = multipartFile.getOriginalFilename();
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
//                            .object(originalFilename)   //文件上传后的文件名
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
     * 创建bucket
     */
    public void createBucket(String bucketName) throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 获取全部bucket
     *
     * @return
     */
    public List<Bucket> getAllBuckets() throws Exception {
        return minioClient.listBuckets();
    }

    /**
     * 根据bucketName获取信息
     *
     * @param bucketName bucket名称
     */
    public Optional<Bucket> getBucket(String bucketName) throws Exception {
        return minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 根据bucketName删除信息
     *
     * @param bucketName bucket名称
     */
    public void removeBucket(String bucketName) throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * 获取⽂件
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @return ⼆进制流
     */
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 上传⽂件
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @param stream     ⽂件流
     * @throws Exception https://docs.minio.io/cn/java-minioClient-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName, InputStream stream) throws
            Exception {
        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, stream.available(), -1).contentType(objectName.substring(objectName.lastIndexOf("."))).build());
    }

    /**
     * 上传⽂件
     *
     * @param bucketName  bucket名称
     * @param objectName  ⽂件名称
     * @param stream      ⽂件流
     * @param size        ⼤⼩
     * @param contextType 类型
     * @throws Exception https://docs.minio.io/cn/java-minioClient-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName, InputStream stream, long
            size, String contextType) throws Exception {
        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, size, -1).contentType(contextType).build());
    }

    //二进制文件转换MultipartFile
    public MultipartFile getMultipartFile(InputStream inputStream) {
        System.out.println("二进制转换MultipartFile开始");
        MockMultipartFile mockMultipartFile = null;
        //java7新特性  不用手动关闭流
        try {
            mockMultipartFile = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("二进制文件转换图片异常");
        }
        return mockMultipartFile;
    }

    /**
     * 获取⽂件信息
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @throws Exception https://docs.minio.io/cn/java-minioClient-api-reference.html#statObject
     */
    public StatObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception {
        return minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

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
     * 删除⽂件
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @throws Exception https://docs.minio.io/cn/java-minioClient-apireference.html#removeObject
     */
    public void removeObject(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 下载文件
     * @param bucketName    桶名
     * @param fileName      文件名
     * @param filePath      文件下载到本地磁盘的路径
     */
    public void downloadFile(String bucketName,String fileName,String filePath) throws Exception{
//        try{
//            minioClient.downloadObject(
//                    DownloadObjectArgs.builder()
//                            .bucket(bucketName)
//                            .object(fileName)
//                            .filename(filePath)
//                            .build());
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new ConditionException("文件下载失败！");
//        }

        minioClient.downloadObject(
                DownloadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .filename(filePath)
                        .build());
    }


}
