package com.foxmo.bilibili.controller.aspect;

import com.foxmo.bilibili.controller.support.UserSupport;
import com.foxmo.bilibili.domain.annotation.ApiLimitedRole;
import com.foxmo.bilibili.domain.auth.UserRole;
import com.foxmo.bilibili.domain.exception.ConditionException;
import com.foxmo.bilibili.service.impl.UserRoleServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class ApiLimitedRoleAspect {
    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    //切点（指定切入的位置）
    @Pointcut("@annotation(com.foxmo.bilibili.domain.annotation.ApiLimitedRole)")
    public void check(){

    }

    //切入后的处理逻辑
    @Before("check() && @annotation(apiLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ApiLimitedRole apiLimitedRole){
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        String[] limitedRoleCodeList = apiLimitedRole.limitedRoleCodeList();
        Set<String> limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        Set<String> userRoleSet = userRoleList.stream().map(UserRole :: getCode).collect(Collectors.toSet());

        //取交集
        userRoleSet.retainAll(limitedRoleCodeSet);
        if (userRoleSet.size() > 0){
            throw new ConditionException("权限不足！");
        }
    }

}
