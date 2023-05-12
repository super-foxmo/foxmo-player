package com.foxmo.bilibili;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

public class test {

    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("my-group1");
        producer.setNamesrvAddr("localhost:9876");
        //Launch the instance.
        producer.start();
        String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 100; i++) {
            int orderId = i % 10;
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTestjjj", tags[i % tags.length], "KEY" + i,
                    ("Hello RocketMQ" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId);

            System.out.printf("%s%n", sendResult);
        }
        //server shutdown
        producer.shutdown();
    }

//        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("HAOKE_IM");
//
//        defaultMQProducer.setNamesrvAddr("127.0.0.1:9876");
//
////        defaultMQProducer.setClientIP("192.168.31.127");
//
//        defaultMQProducer.start();
//
//        /**
//         * key：broker名称 ,newTopic：topic名称, queueNum：队列数（分区）
//         */
//        defaultMQProducer.createTopic("broker_haoke_im", "my_topic", 8);
//
//        System.out.println("创建topic成功");
//
//        defaultMQProducer.shutdown();


    @Test
    public void test1(){
        String[] split = "4-9 测试断点续传.ts".split("\\.");
        for (int i = 0;i < split.length;i++){
            System.out.println(split[i]);
        }
    }
}


