package com.yyp.xrecyclerview.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/** 底部上拉加载
 * Created by yyp on 2017/7/18.
 */
public class LoadMoreFooter extends LinearLayout {
    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    private ProgressBar progressBar;
    private TextView mText;
    private String loadingHint;
    private String noMoreHint;
    private String loadingDoneHint;

    public LoadMoreFooter(Context context) {
        super(context);
        initView();
    }

    public LoadMoreFooter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadMoreFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setLoadingHint(String hint) {
        loadingHint = hint;
    }

    public void setNoMoreHint(String hint) {
        noMoreHint = hint;
    }

    public void setLoadingDoneHint(String hint) {
        loadingDoneHint = hint;
    }

    public void initView(){

        RecyclerView.LayoutParams mLayoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(20,20,20,20);
        setLayoutParams(mLayoutParams);
        setGravity(Gravity.CENTER);

        progressBar = new ProgressBar(getContext());
        progressBar.setProgress(android.R.style.Widget_ProgressBar_Inverse);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        addView(progressBar);

        mText = new TextView(getContext());
        mText.setText("正在加载...");
        loadingHint = "正在加载...";
        noMoreHint = "已经到底了";
        loadingDoneHint = "加载完成";
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(40,0,0,0);

        mText.setLayoutParams(layoutParams);
        addView(mText);
    }

    /**
     * 设置状态
     * @param state 状态:1、加载中 2、完成加载 3、到底了
     */
    public void setState(int state){
        switch(state) {
            case STATE_LOADING:
                progressBar.setVisibility(View.VISIBLE);
                mText.setText(loadingHint);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(loadingDoneHint);
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText(noMoreHint);
                progressBar.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }
}
