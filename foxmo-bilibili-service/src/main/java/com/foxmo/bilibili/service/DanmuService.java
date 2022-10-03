package com.foxmo.bilibili.service;

import com.foxmo.bilibili.domain.Danmu;

import java.util.List;
import java.util.Map;

public interface DanmuService {
    void addDanmu(Danmu danmu);

    List<Danmu> getDanmuList(Long videoId,
                             String startDate,
                             String endDate);
}
