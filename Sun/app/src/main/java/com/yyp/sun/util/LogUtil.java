package com.yyp.sun.util;

/**
 * Log 工具
 * Created by yyp on 2016/8/15.
 */
public class LogUtil {
    public static final String TAG = "SUN";

    public static void i(String s) {
        android.util.Log.i(TAG, s);
    }

    public static void e(String s) {
        android.util.Log.e(TAG, s);
    }
}
