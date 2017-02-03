package com.yyp.sun.util;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片工具
 * Created by yyp on 2016/8/13.
 */
public class ImageUtil {

    //保存头像到本地
    public static Uri saveImage(Bitmap bm, String fileName, String path){
        if(VerifyUtil.hasSdcard()){
            File foder = new File(path);
            File headImage = null;
            try{
                if (!foder.exists()) {
                    foder.mkdirs();
                }
                headImage = new File(path, fileName);
                if (!headImage.exists()) {
                    headImage.createNewFile();
                }else {
                    headImage.delete();
                    headImage.createNewFile();
                }
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(headImage));
                bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                bos.flush();
                bos.close();

            }catch (IOException e){
                e.printStackTrace();
            }
            return Uri.fromFile(headImage);
        }else{
            return null;
        }
    }
}
