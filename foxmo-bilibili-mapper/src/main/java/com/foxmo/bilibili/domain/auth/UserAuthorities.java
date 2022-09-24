package com.foxmo.bilibili.domain.auth;

import org.apache.catalina.LifecycleState;

import java.util.List;

public class UserAuthorities {
    private List<AuthRoleElementOperation> roleElementOperationList;

    private List<AuthRoleMenu> roleMenuList;

    public UserAuthorities() {
    }

    public UserAuthorities(List<AuthRoleElementOperation> roleElementOperationList, List<AuthRoleMenu> roleMenuList) {
        this.roleElementOperationList = roleElementOperationList;
        this.roleMenuList = roleMenuList;
    }

    public List<AuthRoleElementOperation> getRoleElementOperationList() {
        return roleElementOperationList;
    }

    public void setRoleElementOperationList(List<AuthRoleElementOperation> roleElementOperationList) {
        this.roleElementOperationList = roleElementOperationList;
    }

    public List<AuthRoleMenu> getRoleMenuList() {
        return roleMenuList;
    }

    public void setRoleMenuList(List<AuthRoleMenu> roleMenuList) {
        this.roleMenuList = roleMenuList;
    }
}
