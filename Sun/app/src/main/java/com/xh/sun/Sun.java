package com.xh.sun;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.xh.sun.config.SunInfo;
import com.xh.sun.dao.DaoSession;

import cn.bmob.v3.Bmob;

public class Sun extends Application {

    private static Application mContext;

    public DaoSession daoSession;

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
