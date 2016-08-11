package com.yyp.sun;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by yyp on 2016/8/11.
 */
public class Sun extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化图片加载控件
        Fresco.initialize(this);
    }
}
