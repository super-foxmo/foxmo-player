package com.foxmo.bilibili.config;

import com.foxmo.bilibili.config.properties.MinioProperties;
import com.foxmo.bilibili.domain.exception.ConditionException;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class MinioConfig {
    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient(){
        //创建minion客户端
        MinioClient minioClient = MinioClient
                .builder()
                .endpoint(minioProperties.getEndpoint())    //minion链接
                .credentials(minioProperties.getAccessKey(),minioProperties.getSecretKey())
                .build();

        if (minioClient == null){
            throw new ConditionException("minio链接失败！");
        }
        return minioClient;
    }
}
