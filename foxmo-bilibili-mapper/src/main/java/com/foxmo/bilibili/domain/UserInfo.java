package com.foxmo.bilibili.domain;

import java.util.Date;

public class UserInfo {
    private Long id;
    private Long userId;
    private String nick;
    private String sign;
    private String avatar;
    private String gender;
    private String birth;
    private Date createTime;
    private Date updateTime;

    public UserInfo() {
    }

    public UserInfo(Long id, Long userId, String nick, String sign, String avatar, String gender, String birth, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.nick = nick;
        this.sign = sign;
        this.avatar = avatar;
        this.gender = gender;
        this.birth = birth;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
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
