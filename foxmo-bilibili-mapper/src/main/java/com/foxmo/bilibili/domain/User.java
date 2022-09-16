package com.foxmo.bilibili.domain;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

public class User {
    private Long id;
    private String phone;
    private String email;
    private String password;
    private String salt;
    private Date createTime;
    private Date updateTime;

    @TableField(exist = false)  //表示该属性不属于数据库字段
    private UserInfo userInfo;

    public User() {
    }

    public User(Long id, String phone, String email, String password, String salt, Date createTime, Date updateTime, UserInfo userInfo) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.userInfo = userInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
