package com.foxmo.bilibili.controller;

import com.foxmo.bilibili.controller.support.UserSupport;
import com.foxmo.bilibili.domain.JsonResponse;
import com.foxmo.bilibili.domain.User;
import com.foxmo.bilibili.domain.UserInfo;
import com.foxmo.bilibili.service.impl.UserServiceImpl;
import com.foxmo.bilibili.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @PutMapping("/users")
    public JsonResponse<String> modifyUser(@RequestBody User user) throws Exception{
        //获取当前用户ID
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.modifyUser(user);
        return JsonResponse.success();
    }

    @ResponseBody
    @PutMapping("/user-infos")
    public JsonResponse<String> modifyUserInfo(@RequestBody UserInfo userInfo){
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.modifyUserInfo(userInfo);
        return JsonResponse.success();
    }


}
