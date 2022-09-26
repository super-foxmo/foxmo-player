package com.foxmo.bilibili.domain;

import java.util.Date;

public class VideoCollection {
    private Long id;

    private Long videoId;

    private Long UserId;

    private Long groupId;

    private Date createTime;

    public VideoCollection() {
    }

    public VideoCollection(Long id, Long videoId, Long userId, Long groupId, Date createTime) {
        this.id = id;
        this.videoId = videoId;
        UserId = userId;
        this.groupId = groupId;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
