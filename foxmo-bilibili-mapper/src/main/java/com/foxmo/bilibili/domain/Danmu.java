package com.foxmo.bilibili.domain;

import java.util.Date;

public class Danmu {
    private Long id;

    private Long userId;

    private Long videoId;

    private String content;     //弹幕内容

    private String danmuTime;   //弹幕出现时间

    private Date createTime;

    public Danmu() {
    }

    public Danmu(Long id, Long userId, Long videoId, String content, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.videoId = videoId;
        this.content = content;
        this.createTime = createTime;
    }


    public Danmu(Long id, Long userId, Long videoId, String content, String danmuTime, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.videoId = videoId;
        this.content = content;
        this.danmuTime = danmuTime;
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDanmuTime() {
        return danmuTime;
    }

    public void setDanmuTime(String danmuTime) {
        this.danmuTime = danmuTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
