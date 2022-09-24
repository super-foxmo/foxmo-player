package com.foxmo.bilibili.domain.auth;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

public class AuthRoleMenu {
    private Long id;
    private Long roleId;
    private Long menuId;
    private Date createTime;

    @TableField(exist = false)
    private AuthMenu authMenu;

    public AuthRoleMenu() {
    }

    public AuthRoleMenu(Long id, Long roleId, Long menuId, Date createTime, AuthMenu authMenu) {
        this.id = id;
        this.roleId = roleId;
        this.menuId = menuId;
        this.createTime = createTime;
        this.authMenu = authMenu;
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

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public AuthMenu getAuthMenu() {
        return authMenu;
    }

    public void setAuthMenu(AuthMenu authMenu) {
        this.authMenu = authMenu;
    }
}
