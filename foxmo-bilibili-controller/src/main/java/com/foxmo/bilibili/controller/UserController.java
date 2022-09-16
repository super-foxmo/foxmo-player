package com.foxmo.bilibili.controller;

import com.foxmo.bilibili.controller.support.UserSupport;
import com.foxmo.bilibili.domain.JsonResponse;
import com.foxmo.bilibili.domain.User;
import com.foxmo.bilibili.service.impl.UserServiceImpl;
import com.foxmo.bilibili.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserSupport userSupport;

    /**
     * 获取 RSA 公钥
     * @return
     */
    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey(){
        String publicKeyStr = RSAUtil.getPublicKeyStr();
        return new JsonResponse<String>(publicKeyStr);
    }

    /**
     * 注册新用户
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping("/users")
    public JsonResponse<String> addUser(@RequestBody User user){
        //注册新用户
        userService.addUser(user);
        return JsonResponse.success();
    }

    @ResponseBody
    @PostMapping("/user-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception{
        String token = userService.login(user);
        return JsonResponse.success(token);
    }

    @ResponseBody
    @GetMapping("/users")
    public JsonResponse<User> getUserInfo(){
        //获取当前登录用户userId
        Long userId = userSupport.getCurrentUserId();
        User user = userService.queryUserById(userId);
        return new JsonResponse<>(user);
    }


}
