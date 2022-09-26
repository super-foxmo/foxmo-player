package com.foxmo.bilibili.domain;

import java.util.Date;

public class VideoCoin {
    private Long id;

    private Long userId;

    private Long videoId;

    private Long amount;

    private Date createTime;

    private Date updateTime;

    public VideoCoin() {
    }

    public VideoCoin(Long id, Long userId, Long videoId, Long amount, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.videoId = videoId;
        this.amount = amount;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
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
}
