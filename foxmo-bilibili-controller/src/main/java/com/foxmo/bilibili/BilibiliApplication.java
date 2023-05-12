package com.foxmo.bilibili;

import com.foxmo.bilibili.service.impl.websocket.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableAsync    //开启异步线程
@EnableScheduling   //开启定时任务功能
@EnableTransactionManagement
@SpringBootApplication
public class BilibiliApplication {
    public static void main(String[] args) {
        log.info("项目启动成功");
        ApplicationContext applicationContext = SpringApplication.run(BilibiliApplication.class,args);
        WebSocketService.setApplicationContext(applicationContext);
    }
}
