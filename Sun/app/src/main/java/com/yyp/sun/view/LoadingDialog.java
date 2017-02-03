package com.yyp.sun.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.victor.loading.rotate.RotateLoading;
import com.yyp.sun.R;

/**
 * 自定义加载框
 * Created by yyp on 2016/8/13.
 */
public class LoadingDialog extends Dialog {

    private Context mContext;
    private View customView;
    private RotateLoading loadView;

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    /**
     * 初始化自定义的Dialog布局
     * @param msg
     */
    public void initDialog(String msg){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // 得到加载 view
        customView = inflater.inflate(R.layout.dialog_loading, null);
        // 加载圈
        loadView = (RotateLoading) customView.findViewById(R.id.rotateLoading);
        // 提示文字
        TextView tip = (TextView) customView.findViewById(R.id.loading_tip);
        // 加载圈转动
        loadView.start();
        // 设置提示信息
        tip.setText(msg);

        setContentView(customView);
    }

    /**
     * 停止加载圈的转动
     */
    public void stopRotateLoading(){
        if(loadView.isStart()){
            loadView.stop();
        }
    }
}
