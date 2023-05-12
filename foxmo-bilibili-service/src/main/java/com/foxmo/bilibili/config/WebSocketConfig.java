package com.foxmo.bilibili.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "dev")
@Configuration
public class WebSocketConfig {
    //发现websocket服务（标识作用）
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}

