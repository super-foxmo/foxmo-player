package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.User;
import com.foxmo.bilibili.domain.UserInfo;
import com.foxmo.bilibili.domain.constant.UserConstant;
import com.foxmo.bilibili.domain.exception.ConditionException;
import com.foxmo.bilibili.mapper.UserMapper;
import com.foxmo.bilibili.service.UserService;
import com.foxmo.bilibili.util.MD5Util;
import com.foxmo.bilibili.util.RSAUtil;
import com.foxmo.bilibili.util.TokenUtil;
import com.mysql.cj.Constants;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public void addUser(User user) {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号码不能为空");
        }else if(queryUserByPhone(phone) != null){
            throw new ConditionException("该手机号码已注册过");
        }
        //获取当前时间
        Date now = new Date();
        //盐值
        String salt = String.valueOf(now.getTime());
        //该密码为前端进行了 RSA 加密后的密码
        String password = user.getPassword();
        //原始密码
        String rawPassword;
        //解码
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解码错误！");
        }
        //对原始密码进行 MD5 加密
        String MD5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        //封装 user 对象
        user.setSalt(salt);
        user.setPassword(MD5Password);
        user.setCreateTime(now);
        //调用 mapper 层方法，新增 user
        Integer integer = userMapper.insertUser(user);

        //封装 userInfo 对象
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setCreateTime(now);
        //调用 mapper 层方法，新增用户信息
        Integer integer1 = addUserInfo(userInfo);

    }

    @Override
    public String login(User user) throws Exception{
        String phone = user.getPhone();
        //查询数据库是否有该账号
        User dbUser = userMapper.selectUserByPhone(phone);
        if(dbUser == null){
            throw new ConditionException("当前用户不存在！");
        }
        //该密码为前端进行了 RSA 加密后的密码
        String password = user.getPassword();
        //原始密码
        String rawPassword;
        //解码
        try {
            //RSA解密
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解码错误！");
        }
        //对原始密码进行 MD5 加密
        String MD5Password = MD5Util.sign(rawPassword, dbUser.getSalt(), "UTF-8");

        log.info("==============rawPassword = {}================",rawPassword);
        log.info("==============MD5Password = {}================",MD5Password);

        if (!MD5Password.equals(dbUser.getPassword())){
            throw new ConditionException("密码错误！");
        }
        //生成用户身份令牌
        return TokenUtil.generateToken(dbUser.getId());
    }

    @Override
    public User queryUserById(Long userId) {
        User user = userMapper.selectUserById(userId);
        UserInfo userInfo = userMapper.selectUserInfoByUserId(userId);
        user.setUserInfo(userInfo);
        return user;
    }

    /**
     * 修改用户信息
     * @param user
     * @return 1：成功   0：失败
     * @throws Exception
     */
    @Override
    public Integer modifyUser(User user) throws Exception{
        Long id = user.getId();
        //判断数据库中是否有该账号信息
        User dbUser = userMapper.selectUserById(id);
        if (dbUser == null){
            throw new ConditionException("用户不存在！");
        }
        String password = user.getPassword();
        String rawPassword;
        if (!StringUtils.isNullOrEmpty(password)){
            //RSA解密
            rawPassword = RSAUtil.decrypt(password);
            //MD5加密
            String MD5Password = MD5Util.sign(rawPassword,dbUser.getSalt(),"UTF-8");
            user.setPassword(MD5Password);
        }
        user.setUpdateTime(new Date());

        return userMapper.updateUser(user);
    }

    /**
     * 修改用户详细资料
     * @param userInfo
     */
    @Override
    public void modifyUserInfo(UserInfo userInfo) {
        Long userId = userInfo.getUserId();
        User user = userMapper.selectUserById(userId);
        if (user == null){
            throw new ConditionException("用户不存在！");
        }
        userInfo.setUpdateTime(new Date());
        userMapper.updateUserInfo(userInfo);
    }

    @Override
    public List<UserInfo> getUserInfosByIds(Set<Long> followingIdSet) {
        return userMapper.selectUserInfosByIds(followingIdSet);
    }


    @Override
    public User queryUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public Integer addUserInfo(UserInfo userInfo) {
        return userMapper.insertUserInfo(userInfo);
    }


}
