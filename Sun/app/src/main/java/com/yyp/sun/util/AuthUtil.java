package com.yyp.sun.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yyp.sun.Sun;
import com.yyp.sun.ui.user.data.UserInfo;

import cn.bmob.v3.BmobUser;

/**
 * Created by yyp on 2016/8/12.
 */
public class AuthUtil {


    public static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Sun.getContext());


    /**
     * 判断用户是否登录
     * @return 是否登录
     */
    public static boolean isLogin(){
        return getCurrentUser() != null;
    }

    /**
     * 获取当前用户
     * @return 当前用户
     */
    public static UserInfo getCurrentUser(){
        return BmobUser.getCurrentUser(UserInfo.class);
    }
}
