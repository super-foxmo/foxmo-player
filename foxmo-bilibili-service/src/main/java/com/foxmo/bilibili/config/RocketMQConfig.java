package com.foxmo.bilibili.config;

import com.alibaba.fastjson.JSONObject;
import com.foxmo.bilibili.domain.UserFollowing;
import com.foxmo.bilibili.domain.UserMoment;
import com.foxmo.bilibili.domain.constant.UserDanmusConstant;
import com.foxmo.bilibili.domain.constant.UserMomentsConstant;
import com.foxmo.bilibili.service.impl.UserFollowingServiceImpl;
import com.foxmo.bilibili.service.impl.websocket.WebSocketService;
import com.mysql.cj.util.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RocketMQConfig {

    @Value("${rocketmq.name.server.address}")
    private String nameServerAddr;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    UserFollowingServiceImpl userFollowingService;

    @Bean("momentsProducer")
    public DefaultMQProducer momentsProducer() throws  Exception{
        //实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.GROUP_MOMENTS);
        //设置NameServer的地址
        producer.setNamesrvAddr(nameServerAddr);
        //启动Producer实例
        producer.start();
        return producer;
    }

    @Bean("momentsConsumer")
    public DefaultMQPushConsumer momentsConsumer() throws Exception{
        //实例化消费者（推送方式）
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.GROUP_MOMENTS);
        //设置NameServer的地址
        consumer.setNamesrvAddr(nameServerAddr);
        //订阅一个或多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe(UserMomentsConstant.TOPIC_MOMENTS,"*");
        //注册监听器，实时监听关注用户的动态信息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext Context) {
                MessageExt message = msgs.get(0);
                if (message == null){
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }else{
                    String bodyStr = new String(message.getBody());

                    UserMoment userMoment = JSONObject.toJavaObject(JSONObject.parseObject(bodyStr), UserMoment.class);
                    //获取当前用户id
                    Long userId = userMoment.getUserId();
                    //获取当前用户的所有粉丝
                    List<UserFollowing> fenList = userFollowingService.getUsaerFensByUserId(userId);
                    //给每个粉丝推送动态发布信息
                    for (UserFollowing fen : fenList){
                        String key = "subscribed-" + fen.getUserId();
                        //获取该粉丝关注的所有用户的动态推送信息（String格式）
                        String subscribedListStr = redisTemplate.opsForValue().get(key);
                        //动态推送列表
                        List<UserMoment> subscribedList;
                        if (StringUtils.isNullOrEmpty(subscribedListStr)){  //该粉丝当前没有任何动态推送信息
                            subscribedList = new ArrayList<>();
                        }else{  //该粉丝当前有动态推送信息
                            //将字符串格式的动态推送信息转换成列表格式（String -> List）
                            subscribedList = JSONObject.parseArray(subscribedListStr,UserMoment.class);
                        }
                        //将新的动态推送信息添加到列表中
                        subscribedList.add(userMoment);
                        //将动态推送列表存储到redis中
                        redisTemplate.opsForValue().set(key,JSONObject.toJSONString(subscribedList));
                    }

                }
                //标记该消息已经被成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动Consumer实例
        consumer.start();
        return consumer;
    }

    @Bean("danmusProducer")
    public DefaultMQProducer danmusProducer() throws  Exception{
        //实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer(UserDanmusConstant.GROUP_DANMUS);
        //设置NameServer的地址
        producer.setNamesrvAddr(nameServerAddr);
        //启动Producer实例
        producer.start();
        return producer;
    }

    @Bean("danmusConsumer")
    public DefaultMQPushConsumer danmusConsumer() throws Exception{
        //实例化消费者（推送方式）
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserDanmusConstant.GROUP_DANMUS);
        //设置NameServer的地址
        consumer.setNamesrvAddr(nameServerAddr);
        //订阅一个或多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe(UserDanmusConstant.TOPIC_DANMUS,"*");
        //注册监听器，实时监听关注用户的动态信息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext Context) {
                MessageExt msg = msgs.get(0);
                String bodyStr = new String(msg.getBody());
                JSONObject jsonObject = JSONObject.parseObject(bodyStr);
                String sessionId = jsonObject.getString("sessionId");
                String message = jsonObject.getString("message");

                WebSocketService webSocketService = WebSocketService.WEBSOCKET_MAP.get(sessionId);
                //判断用户与服务器是否已建立连接
                if (webSocketService.getSession().isOpen()){
                    try{
                        webSocketService.sendMessage(message);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                //标记该消息已经被成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        return consumer;
    }
}
