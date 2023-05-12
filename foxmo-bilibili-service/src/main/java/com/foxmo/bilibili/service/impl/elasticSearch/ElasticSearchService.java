package com.foxmo.bilibili.service.impl.elasticSearch;

import com.foxmo.bilibili.domain.Video;
import com.foxmo.bilibili.domain.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ElasticSearchService {
    @Autowired
    private VideoRepository videoRepository;

    public void addVideo(Video video){
        videoRepository.save(video);
    }
}
