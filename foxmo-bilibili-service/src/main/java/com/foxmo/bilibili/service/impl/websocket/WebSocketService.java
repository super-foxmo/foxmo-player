package com.foxmo.bilibili.service.impl.websocket;

import com.alibaba.fastjson.JSONObject;
import com.foxmo.bilibili.config.WebSocketConfig;
import com.foxmo.bilibili.domain.Danmu;
import com.foxmo.bilibili.domain.constant.UserDanmusConstant;
import com.foxmo.bilibili.service.impl.DanmuServiceImpl;
import com.foxmo.bilibili.util.RocketMQUtil;
import com.foxmo.bilibili.util.TokenUtil;
import com.mysql.cj.util.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ConditionalOnClass(value = WebSocketConfig.class)
@ServerEndpoint("/websocket/{token}")
public class WebSocketService {     //WebSocket为多例模式
    //日志
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //当前在线人数（AtomicInteger：线程安全）
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    //每一个链接用户的WebSocketService（ConcurrentHashMap：线程安全）
    public static final ConcurrentHashMap<String,WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();
    //会话
    private Session session;
    //会话ID
    private String sessionId;
    //用户ID
    private Long userId;

    private static ApplicationContext APPLICATION_CONTEXT;

    //为多例模式Bean注入准备
    public static void setApplicationContext(ApplicationContext applicationContext){
        WebSocketService.APPLICATION_CONTEXT = applicationContext;
    }

    //请求连接
    @OnOpen
    public void openConnection(Session session, @PathParam("token") String token){
        //登录模式和游客模式
        try{
            this.userId = TokenUtil.verifyToken(token);
        }catch (Exception e){}

        this.sessionId = session.getId();
        this.session = session;
        //判断 WEBSOCKET_MAP 中是否已有该用户sessionId
        if (WEBSOCKET_MAP.containsKey(sessionId)){
            //删除原有WebSocketService
            WEBSOCKET_MAP.remove(sessionId);
            //保存当前连接用户的WebSocketService
            WEBSOCKET_MAP.put(sessionId,this);
        }else {
            //保存当前连接用户的WebSocketService
            WEBSOCKET_MAP.put(sessionId,this);
            //在线人数加一
            ONLINE_COUNT.getAndIncrement();
        }
        logger.info("用户连接成功：" + sessionId + ",当前在线人数为：" + ONLINE_COUNT.get());
        try{
            //通知前端，用户连接成功
            this.sendMessage("0");
        }catch (Exception e){
            logger.error("连接失败！");
        }
    }

    //断开连接
    @OnClose
    public void closeConnection(){
        if (WEBSOCKET_MAP.containsKey(sessionId)){
            //删除当前连接用户保存的WebSocketService
            WEBSOCKET_MAP.remove(sessionId);
            //在线人数减一
            ONLINE_COUNT.getAndDecrement();
        }
        logger.info("用户退出：" + sessionId + "，当前在线人数为：" + ONLINE_COUNT.get());
    }

    //接收消息后的处理
    @OnMessage
    public void OnMessage(String message){
        logger.info("用户信息：" + sessionId + ",报文：" + message);
        if (!StringUtils.isNullOrEmpty(message)){
            try{
                //群发消息
                for (Map.Entry<String,WebSocketService> entry : WEBSOCKET_MAP.entrySet()){
                    WebSocketService webSocketService = entry.getValue();
                    //获取生产者
                    DefaultMQProducer danmusProducer = (DefaultMQProducer)APPLICATION_CONTEXT.getBean("danmusProducer");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message",message);
                    jsonObject.put("sessionId",sessionId);
                    //封装消息对象
                    Message msg = new Message(UserDanmusConstant.TOPIC_DANMUS, jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
                    //异步发送消息
                    RocketMQUtil.asyncSendMsg(danmusProducer,msg);
                }
                //判断用户是否已登陆
                if (this.userId != null){
                    //异步保存弹幕到数据库
                    Danmu danmu = JSONObject.parseObject(message, Danmu.class);
                    danmu.setUserId(userId);
                    danmu.setCreateTime(new Date());
                    DanmuServiceImpl danmuServiceImpl = (DanmuServiceImpl) APPLICATION_CONTEXT.getBean("danmuServiceImpl");
                    danmuServiceImpl.asyncAddDanmu(danmu);
                    //保存弹幕到redis
                    danmuServiceImpl.addDanmuToRedis(danmu);
                }
            }catch (Exception e){
                logger.error("弹幕接收出现问题");
                e.printStackTrace();
            }
        }
    }

    //定时发送当前在线人数到前端
    @Scheduled(fixedRate = 5000) //5秒
    private void noticeOnlineCount() throws Exception {
        for (Map.Entry<String,WebSocketService> entry : WebSocketService.WEBSOCKET_MAP.entrySet()){
            WebSocketService webSocketService = entry.getValue();
            //判断是否在线
            if (webSocketService.getSession().isOpen()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("onlineCount",ONLINE_COUNT);
                jsonObject.put("msg","当前在线人数为：" + ONLINE_COUNT);
                //给在线用户发送信息
                webSocketService.sendMessage(jsonObject.toJSONString());
            }
        }
    }

    //发生错误后的处理
    @OnError
    public void OnError(String error){

    }

    //发送信息给客户端
    public void sendMessage(String message) throws Exception{
        this.session.getBasicRemote().sendText(message);
    }

    public Session getSession() {
        return session;
    }

    public String getSessionId() {
        return sessionId;
    }
}
