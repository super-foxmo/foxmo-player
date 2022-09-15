package com.foxmo.bilibili;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class BilibiliApplication {
    public static void main(String[] args) {
        log.info("项目启动成功");
        ApplicationContext applicationContext = SpringApplication.run(BilibiliApplication.class,args);
    }
}
