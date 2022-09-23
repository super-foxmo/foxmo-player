package com.foxmo.bilibili.util;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;

public class RocketMQUtil {
    //同步发送
    public static void syncSendMsg(DefaultMQProducer producer, Message msg) throws Exception{

        SendResult result = producer.send(msg);
        System.out.println(msg);
    }

    //异步发送
    public static void asyncSendMsg(DefaultMQProducer producer,Message msg) throws Exception{
        int messageCount = 2;
        CountDownLatch2 countDownLatch2 = new CountDownLatch2(messageCount);
        for (int i = 0;i < messageCount;i++){
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch2.countDown();
                    System.out.println(sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable throwable) {
                    countDownLatch2.countDown();
                    System.out.println("发送消息时发生的异常：" + throwable);
                    throwable.printStackTrace();
                }
            });
        }
        countDownLatch2.await(5, TimeUnit.SECONDS);
    }
}
