package com.xh.sun.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.xh.sun.R;

/**
 * 选择性别的提示框
 */
public class SexSelectDialog extends Dialog {

    private Context mContext;
    private View customView;

    public SexSelectDialog(Context context) {
        super(context);
        this.mContext = context;

        initDialog();
    }

    private void initDialog(){
        customView = LayoutInflater.from(mContext).inflate(R.layout.dialog_sex_select,null);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //不加标题
        setContentView(customView); // 调用Dialog的setContentView(View v);
    }

    /**
     * 方便外部获取
     * @return customView
     */
    public View getCustomView(){
        return customView;
    }
}