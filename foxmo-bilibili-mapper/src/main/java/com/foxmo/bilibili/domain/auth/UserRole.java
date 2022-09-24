package com.foxmo.bilibili.domain.auth;

import java.util.Date;

public class UserRole {
    private Long id;
    private Long userId;
    private Long roleId;
    private String Name;
    private String Code;
    private Date createTime;

    public UserRole() {
    }

    public UserRole(Long id, Long userId, Long roleId, String name, String code, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
        Name = name;
        Code = code;
        this.createTime = createTime;
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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
