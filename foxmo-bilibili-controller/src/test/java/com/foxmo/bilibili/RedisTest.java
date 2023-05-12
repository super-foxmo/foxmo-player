package com.foxmo.bilibili;

import com.alibaba.fastjson.JSONObject;
import com.foxmo.bilibili.domain.Danmu;
import com.mysql.cj.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void test1(){
        redisTemplate.opsForValue().set("Hello","World");

        Object hello = redisTemplate.opsForValue().get("Hello");

        System.out.println(hello);

        redisTemplate.delete("Hello");
        redisTemplate.delete("danmu-video-" + 1L);
    }

    @Test
    public void test2(){
        ArrayList<Danmu> danmus = new ArrayList<>();
        danmus.add(new Danmu(1L,1L,1L,"钢铁侠牛逼",null,new Date()));
        danmus.add(new Danmu(2L,2L,1L,"钢铁侠最强",null,new Date()));
        danmus.add(new Danmu(3L,3L,1L,"蜘蛛侠好帅",null,new Date()));
        danmus.add(new Danmu(4L,4L,1L,"雷神好搞笑",null,new Date()));
        redisTemplate.delete("danmu-video-" + danmus.get(1).getVideoId());

        for (Danmu danmu : danmus){
            addDanmuToRedis(danmu);
            String i = redisTemplate.opsForValue().get("danmu-video-" + danmus.get(1).getVideoId());
        }

        String danmuStr = redisTemplate.opsForValue().get("danmu-video-" + danmus.get(1).getVideoId());
        System.out.println(danmuStr);

        redisTemplate.delete("danmu-video-" + danmus.get(1).getVideoId());
    }

    public void addDanmuToRedis(Danmu danmu){
        String key = "danmu-video-" + danmu.getVideoId();
        String value = redisTemplate.opsForValue().get(key);
        List<Danmu> list = new ArrayList<>();

        if (!StringUtils.isNullOrEmpty(value)){
            list = JSONObject.parseArray(value, Danmu.class);
        }
        list.add(danmu);
        redisTemplate.opsForValue().set(key,JSONObject.toJSONString(list));
    }

}
