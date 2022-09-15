package com.foxmo.bilibili.controller;

import com.foxmo.bilibili.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @ResponseBody
    @GetMapping("/queryName/{id}")
    public String queryNameById(@PathVariable("id") Integer id){
        return userService.queryNameById(id);
    }
}
