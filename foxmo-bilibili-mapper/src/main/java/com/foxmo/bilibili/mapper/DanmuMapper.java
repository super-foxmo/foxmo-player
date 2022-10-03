package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.Danmu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DanmuMapper {

    void insertDanmu(Danmu danmu);

    List<Danmu> selectDanmuList(Map<String,Object> params);
}
