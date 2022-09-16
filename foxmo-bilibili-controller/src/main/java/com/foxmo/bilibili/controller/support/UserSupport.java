package com.foxmo.bilibili.controller.support;

import com.foxmo.bilibili.domain.exception.ConditionException;
import com.foxmo.bilibili.util.RSAUtil;
import com.foxmo.bilibili.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserSupport {
    /**
     * 用户登录后，服务器会返回一个token给客户端，客户端保存该token，
     * 并且发送请求的时候都会携带该token，token放置在请求头中
     * @return  当前登录用户的userId
     */
    public Long getCurrentUserId(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");
        Long userId = TokenUtil.verifyToken(token);
        if (userId < 0){
            throw new ConditionException("非法用户！");
        }
        return userId;
    }
}
