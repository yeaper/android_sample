package com.yyp.sun;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.yyp.sun.config.SunInfo;

import cn.bmob.v3.Bmob;

/**
 * Created by yyp on 2016/8/11.
 */
public class Sun extends Application {

    private static Application mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // 初始化图片加载控件
        Fresco.initialize(this);
        // 初始化Bmob
        Bmob.initialize(this, SunInfo.APPLICATION_ID);
    }

    public static Context getContext() {
        return mContext;
    }
}
