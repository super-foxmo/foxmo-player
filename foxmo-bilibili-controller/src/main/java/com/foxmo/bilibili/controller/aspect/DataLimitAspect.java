package com.foxmo.bilibili.controller.aspect;

import com.foxmo.bilibili.controller.support.UserSupport;
import com.foxmo.bilibili.domain.UserMoment;
import com.foxmo.bilibili.domain.annotation.ApiLimitedRole;
import com.foxmo.bilibili.domain.auth.UserRole;
import com.foxmo.bilibili.domain.constant.AuthRoleConstant;
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
public class DataLimitAspect {
    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Pointcut("@annotation(com.foxmo.bilibili.domain.annotation.DataLimited)")
    public void check(){

    }

    @Before("check()")
    public void doBefore(JoinPoint joinPoint){
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        Set<String> userRoleSet = userRoleList.stream().map(UserRole :: getCode).collect(Collectors.toSet());

        //获取切入方法的参数
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof UserMoment){
                UserMoment userMoment = (UserMoment) arg;
                String type = userMoment.getType();
                if (userRoleSet.contains(AuthRoleConstant.ROLE_LV0) && !"0".equals(type)){
                    throw new ConditionException("非法参数！");
                }
            }
        }
    }
}
