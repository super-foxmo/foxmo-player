package com.foxmo.bilibili;

import com.foxmo.bilibili.util.MinioUtil;
import io.minio.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MinioTest {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioUtil minioUtil;

    @Test
    public void test1(){
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
//            MinioClient minioClient =
//                    MinioClient.builder()
//                            .endpoint("http://192.168.250.130:9000")
//                            .credentials("admin", "password")
//                            .build();

            // Make 'asiatrip' bucket if not exist.
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("video").build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("video").build());
            } else {
                System.out.println("Bucket 'asiatrip' already exists.");
            }

            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("video")
                            .object("4-11 秒传测试.ts")
                            .filename("D:/学习资料/学习视频/第四章 打造高性能的视频和弹幕系统/4-11 秒传测试.ts")
                            .build());


        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
    }

    //文件下载
    @Test
    public void test3() throws Exception{
        minioClient.downloadObject (
                DownloadObjectArgs.builder()
                        .bucket("video")
                        .object("2.ts")
                        .filename("D:/电影/minio-data/2.ts")
                        .build());
    }
}
