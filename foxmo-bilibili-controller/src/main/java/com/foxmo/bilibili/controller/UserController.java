package com.foxmo.bilibili.controller;

import com.alibaba.fastjson.JSONObject;
import com.foxmo.bilibili.controller.support.UserSupport;
import com.foxmo.bilibili.domain.JsonResponse;
import com.foxmo.bilibili.domain.PageResult;
import com.foxmo.bilibili.domain.User;
import com.foxmo.bilibili.domain.UserInfo;
import com.foxmo.bilibili.domain.exception.ConditionException;
import com.foxmo.bilibili.service.impl.UserFollowingServiceImpl;
import com.foxmo.bilibili.service.impl.UserServiceImpl;
import com.foxmo.bilibili.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserSupport userSupport;

    @Autowired
    UserFollowingServiceImpl userFollowingService;

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

    //用户登录
    @ResponseBody
    @PostMapping("/user-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception{
        String token = userService.login(user);
        return JsonResponse.success(token);
    }

    //双token验证登录
    @ResponseBody
    @PostMapping("/user-dts")
    public JsonResponse<Map<String,Object>> loginfordts(@RequestBody User user) throws Exception{
        Map<String,Object> resultMap = userService.loginForDts(user);
        return new JsonResponse<>(resultMap);
    }

    //退出登录
    @ResponseBody
    @DeleteMapping("/refresh-token")
    public JsonResponse<String> logout(HttpServletRequest request){
        String refreshToken = request.getHeader("refreshToken");
        Long userId = userSupport.getCurrentUserId();
        Integer result = userService.deleteRefreshToken(refreshToken,userId);
        if (result > 0){
            return JsonResponse.success();
        }
        return JsonResponse.fail("505","用户退出失败！");
    }

    //刷新 accessToken
    @ResponseBody
    @PostMapping("/access-token")
    public JsonResponse<String> refreshAccessToken(HttpServletRequest request) throws Exception{
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = userService.refreshAccessToken(refreshToken);

        return new JsonResponse<>(accessToken);
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

    @ResponseBody
    @GetMapping("/user-infos")
    public JsonResponse<PageResult<UserInfo>> pageListUserInfos(
            @RequestParam Integer no,
            @RequestParam Integer size,
            String nick){
        Long userId = userSupport.getCurrentUserId();
        User user = userService.queryUserById(userId);
        if(user == null){
            throw new ConditionException("该用户不存在！");
        }

        //包装查询条件参数
        JSONObject param = new JSONObject();
        param.put("no",no);
        param.put("size",size);
        param.put("nick",nick);
        param.put("userId",userId);
        //查询用户分页数据
        PageResult<UserInfo> pageResult = userService.pageListUserInfo(param);

        List<UserInfo> userInfoList = new ArrayList<>();
        if (pageResult.getTotal() > 0){
            //查询用户的关注状态
            userInfoList = userFollowingService.checkFollowingStatus(pageResult.getList(),userId);
        }
        pageResult.setList(userInfoList);

        return new JsonResponse<>(pageResult);
    }


}
