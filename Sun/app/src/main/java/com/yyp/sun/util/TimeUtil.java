package com.yyp.sun.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * Created by yyp on 2016/8/17.
 */
public class TimeUtil {

    /**
     * 获取当前系统时间
     * @return 当前系统时间
     */
    public static String getSystemTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());

        return formatter.format(curDate);
    }
}
