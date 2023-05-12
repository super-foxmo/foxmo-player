package com.foxmo.bilibili.domain.repository;

import com.foxmo.bilibili.domain.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VideoRepository extends ElasticsearchRepository<Video,Long> {

}
