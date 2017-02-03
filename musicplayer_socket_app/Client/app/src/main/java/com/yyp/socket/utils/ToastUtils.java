package com.yyp.socket.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yyp on 2016/12/9.
 */

public class ToastUtils {

    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
