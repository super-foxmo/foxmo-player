package com.foxmo.bilibili.domain.constant;

public interface UserConstant {
    //性别男
    public static final String GENDER_MALE = "0";
    //性别女
    public static final String GENDER_FEMALE = "1";
    //性别未知
    public static final String GENDER_UNKNOWS = "2";
    //默认生日
    public static final String DEFAULT_BIRTH = "2000-10-10";
    //默认昵称
    public static final String DEFAULT_NICK = "foxmo";
    //关注默认分组
    public static final Long USER_FOLLOWING_GROUP_TYPE_DEFAULT = 3L;
    //自定义分组
    public static final String USER_FOLLOWING_GROUP_TYPE_CUSTOM = "3";
    //全部关注
    public static final String USER_FOLLOWING_GROUP_ALL_NAME = "全部关注";
}
