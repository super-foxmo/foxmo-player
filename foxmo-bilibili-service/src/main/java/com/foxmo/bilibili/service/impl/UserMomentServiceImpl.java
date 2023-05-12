package com.foxmo.bilibili.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.foxmo.bilibili.domain.UserMoment;
import com.foxmo.bilibili.domain.constant.UserMomentsConstant;
import com.foxmo.bilibili.mapper.UserMomentMapper;
import com.foxmo.bilibili.service.UserMomentService;
import com.foxmo.bilibili.util.RocketMQUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class UserMomentServiceImpl implements UserMomentService {

    @Autowired
    UserMomentMapper userMomentMapper;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Override
    public void addUserMoment(UserMoment userMoment) throws Exception{
        userMoment.setCreateTime(new Date());
        userMomentMapper.insertUserMoment(userMoment);
        //获取生产者
        DefaultMQProducer producer = (DefaultMQProducer) applicationContext.getBean("momentsProducer");
        //封装消息
        Message msg = new Message(UserMomentsConstant.TOPIC_MOMENTS, JSONObject.toJSONString(userMoment).getBytes(StandardCharsets.UTF_8));
        //同步发送消息
        RocketMQUtil.syncSendMsg(producer,msg);
    }

    @Override
    public List<UserMoment> getUserSubscribedMoments(Long userId) {
        String key = "subscribed-" + userId;
        //从 redis 中获取当前用户动态推送信息
        String subscribedListStr = redisTemplate.opsForValue().get(key);

        return JSONObject.parseArray(subscribedListStr,UserMoment.class);
    }
}
