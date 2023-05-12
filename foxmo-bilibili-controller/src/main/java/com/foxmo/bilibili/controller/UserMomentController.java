package com.foxmo.bilibili.controller;

import com.foxmo.bilibili.controller.support.UserSupport;
import com.foxmo.bilibili.domain.JsonResponse;
import com.foxmo.bilibili.domain.UserMoment;
import com.foxmo.bilibili.domain.annotation.ApiLimitedRole;
import com.foxmo.bilibili.domain.annotation.DataLimited;
import com.foxmo.bilibili.domain.constant.AuthRoleConstant;
import com.foxmo.bilibili.service.impl.UserMomentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserMomentController {
    @Autowired
    UserSupport userSupport;

    @Autowired
    UserMomentServiceImpl userMomentService;

    //创建用户动态
    @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_LV3})  //基于接口的权限控制
    @DataLimited    //基于数据的权限控制
    @PostMapping("/user-moments")
    public JsonResponse<String> addUserMoment(@RequestBody UserMoment userMoment) throws Exception{
        Long userId = userSupport.getCurrentUserId();
        userMoment.setUserId(userId);
        userMomentService.addUserMoment(userMoment);

        return JsonResponse.success();
    }

    //查询动态推送列表
    @GetMapping("/user-subscribed-moments")
    public JsonResponse<List<UserMoment>> getUserSubscribedMoments(){
        Long userId = userSupport.getCurrentUserId();
        List<UserMoment> userMomentList = userMomentService.getUserSubscribedMoments(userId);

        return new JsonResponse<>(userMomentList);
    }
}
