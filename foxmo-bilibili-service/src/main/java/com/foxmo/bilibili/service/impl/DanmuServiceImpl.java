package com.foxmo.bilibili.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.foxmo.bilibili.domain.Danmu;
import com.foxmo.bilibili.mapper.DanmuMapper;
import com.foxmo.bilibili.service.DanmuService;
import com.mysql.cj.util.StringUtils;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class DanmuServiceImpl implements DanmuService {

    private static final String DANMU_KEY = "dm-video-";

    @Autowired
    private DanmuMapper danmuMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 新增弹幕
     * @param danmu 弹幕对象
     */
    @Override
    public void addDanmu(Danmu danmu) {
        danmuMapper.insertDanmu(danmu);
    }

    /**
     * 异步新增弹幕
     * @param danmu 弹幕对象
     */
    @Async
    public void asyncAddDanmu(Danmu danmu) {
        danmuMapper.insertDanmu(danmu);
    }

    /**
     * 获取指点视频和时间段的所有弹幕
     * 查询策略是优先查询redis中的弹幕数据
     * 如果没有的话查询数据库，然后打查询到的弹幕数据写入到redis中
     * @param videoId   视频ID
     * @param startDate     开始时间
     * @param endDate   截止时间
     * @return
     */
    @Override
    public List<Danmu> getDanmuList(Long videoId,
                                    String startDate,
                                    String endDate) {
        String key = DANMU_KEY + videoId;
        String value = redisTemplate.opsForValue().get(key);
        List<Danmu> list;
        if (!StringUtil.isNullOrEmpty(value)){  //redis中有相关的弹幕数据
            list = JSONObject.parseArray(value, Danmu.class);
            if (！StringUtil.isNullOrEmpty(startDate) && ！StringUtil.isNullOrEmpty(endDate)){  //登录模式
                //设置时间格式
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startTime = dateFormat.parse(startDate);
                Date endTime = dateFormat.parse(endDate);
                ArrayList<Danmu> childList = new ArrayList<>();
                for (Danmu danmu : list){
                    Date createTime = danmu.getCreateTime();
                    //指定时间段
                    if (createTime.before(endTime) && createTime.after(startTime)){
                        childList.add(danmu);
                    }
                }
                list = childList;
            }
            //登录模式
            return list;
        }else{  //redis中没有相关弹幕数据，查询数据库
            HashMap<String, Object> params = new HashMap<>();
            params.put("videoId",videoId);
            params.put("startDate",startDate);
            params.put("endDate",endDate);
            list = danmuMapper.selectDanmuList(params);
            //保存弹幕数据到redis
            redisTemplate.opsForValue().set(key,JSONObject.toJSONString(list));
        }
    }

    /**
     * 保存弹幕到redis
     */
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
