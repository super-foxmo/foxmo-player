package com.foxmo.bilibili.domain;

import cn.hutool.db.DaoTemplate;

import java.util.Date;
import java.util.List;

public class VideoComment {
    private Long id;

    private Long videoId;

    private Long userId;

    private String comment;     //评论内容

    private Long replyUserId;   //回复用户ID

    private Long rootId;    //根节点评论ID

    private Date createTime;

    private Date updateTime;

    private List<VideoComment> childList;   //下一级评论

    private UserInfo userInfo;      //评论用户详细信息

    private UserInfo replyUserInfo;     //回复评论用户详细信息

    public VideoComment() {
    }

    public VideoComment(Long id, Long videoId, Long userId, String comment, Long replyUserId, Long rootId, Date createTime, Date updateTime, List<VideoComment> childList, UserInfo userInfo, UserInfo replyUserInfo) {
        this.id = id;
        this.videoId = videoId;
        this.userId = userId;
        this.comment = comment;
        this.replyUserId = replyUserId;
        this.rootId = rootId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.childList = childList;
        this.userInfo = userInfo;
        this.replyUserInfo = replyUserInfo;
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
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(Long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
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

    public List<VideoComment> getChildList() {
        return childList;
    }

    public void setChildList(List<VideoComment> childList) {
        this.childList = childList;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getReplyUserInfo() {
        return replyUserInfo;
    }

    public void setReplyUserInfo(UserInfo replyUserInfo) {
        this.replyUserInfo = replyUserInfo;
    }
}
