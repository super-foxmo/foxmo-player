package com.foxmo.bilibili.controller;

import com.foxmo.bilibili.controller.support.UserSupport;
import com.foxmo.bilibili.domain.JsonResponse;
import com.foxmo.bilibili.domain.auth.UserAuthorities;
import com.foxmo.bilibili.domain.auth.UserRole;
import com.foxmo.bilibili.service.impl.UserAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserAuthController {
    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    @ResponseBody
    @GetMapping("/user-authorities")
    public JsonResponse<UserAuthorities> getUserAuthoritiesByUserId(){
        Long userId = userSupport.getCurrentUserId();
        UserAuthorities userAuthorities = userAuthService.getUserAuthoritiesByUserId(userId);

        return new JsonResponse<>(userAuthorities);
    }
}
