package com.yyp.sun.config;

import android.os.Environment;

/**
 * Created by yyp on 2016/8/12.
 */
public class SunInfo {

    // Bmob 相关
    public static final String APPLICATION_ID = "fec194eeca7e9bab9caa7eeff4247c87";

    // 获取照片的请求识别码
    public static final int CODE_GALLERY_REQUEST = 0xa0;
    public static final int CODE_RESULT_REQUEST = 0xa2;

    // 文件存储路径
    public static final String BASE_FILE_URL = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : Environment.getExternalStorageDirectory().getPath();
    public static final String HEAD_IMAGE_URL = "/sun/HD_Image/";
    public static final String HEAD_IMAGE_NAME = "head.jpg";

}
