package com.foxmo.bilibili.config;

import com.alibaba.fastjson.JSONObject;
import com.foxmo.bilibili.domain.UserFollowing;
import com.foxmo.bilibili.domain.UserMoment;
import com.foxmo.bilibili.domain.constant.UserMomentsConstant;
import com.foxmo.bilibili.service.impl.UserFollowingServiceImpl;
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
    private String nameServerAddr = "127.0.0.1:9876";

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    UserFollowingServiceImpl userFollowingService;

    @Bean("momentsProducer")
    public DefaultMQProducer momentsProducer() throws  Exception{
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.GROUP_MOMENTS);
        producer.setNamesrvAddr(nameServerAddr);
        producer.start();
        return producer;
    }

    @Bean("momentsConsumer")
    public DefaultMQPushConsumer momentsConsumer() throws Exception{
        //推送方式
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.GROUP_MOMENTS);
        consumer.setNamesrvAddr(nameServerAddr);

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
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        return consumer;
    }
}
