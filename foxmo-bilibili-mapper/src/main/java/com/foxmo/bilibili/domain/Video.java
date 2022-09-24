package com.foxmo.bilibili.domain;

import java.util.Date;
import java.util.List;

public class Video {
    private Long id;

    private Long userId;

    private String url;     //视频链接

    private String thumbnail;    //封面链接

    private String title;   //视频标题

    private String duration;    //视频时长

    private String area;    //所在分区

    private String description;     //视频简介

    private Date createTime;

    private Date updateTime;

    private List<VideoTag> videoTagList;    //标签列表

    public Video() {
    }

    public Video(Long id, Long userId, String url, String thumbnail, String title, String duration, String area, String description, Date createTime, Date updateTime, List<VideoTag> videoTagList) {
        this.id = id;
        this.userId = userId;
        this.url = url;
        this.thumbnail = thumbnail;
        this.title = title;
        this.duration = duration;
        this.area = area;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.videoTagList = videoTagList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<VideoTag> getVideoTagList() {
        return videoTagList;
    }

    public void setVideoTagList(List<VideoTag> videoTagList) {
        this.videoTagList = videoTagList;
    }
}
