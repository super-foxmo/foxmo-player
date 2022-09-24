package com.foxmo.bilibili.domain.auth;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

public class AuthRoleElementOperation {
    private Long id;
    private Long roleId;
    private Long elementOperationId;
    private Date createTime;

    @TableField(exist = false)
    private AuthElementOperation authElementOperation;

    public AuthRoleElementOperation() {
    }

    public AuthRoleElementOperation(Long id, Long roleId, Long elementOperationId, Date createTime, AuthElementOperation authElementOperation) {
        this.id = id;
        this.roleId = roleId;
        this.elementOperationId = elementOperationId;
        this.createTime = createTime;
        this.authElementOperation = authElementOperation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getElementOperationId() {
        return elementOperationId;
    }

    public void setElementOperationId(Long elementOperationId) {
        this.elementOperationId = elementOperationId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public AuthElementOperation getAuthElementOperation() {
        return authElementOperation;
    }

    public void setAuthElementOperation(AuthElementOperation authElementOperation) {
        this.authElementOperation = authElementOperation;
    }
}
