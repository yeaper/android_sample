package com.yyp.sun.ui.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yyp.sun.R;
import com.yyp.sun.ui.app.data.FeedBack;
import com.yyp.sun.util.AuthUtil;
import com.yyp.sun.util.LogUtil;
import com.yyp.sun.util.ToastUtil;
import com.yyp.sun.util.VerifyUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class FeedBackActivity extends AppCompatActivity {

    ActionBar actionbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.feedback_content)
    EditText feedbackContent;

    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        c = FeedBackActivity.this;

        initToolbar();
    }

    /**
     * 初始化头部
     */
    public void initToolbar() {
        // ToolBar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        // 设置标题
        toolbar_title.setText(R.string.app_about_feedback);
        setSupportActionBar(toolbar);

        actionbar = getSupportActionBar();
        if (actionbar != null) {
            // 设置返回按钮
            actionbar.setDisplayHomeAsUpEnabled(true);
            // 去掉 ActionBar 自带标题
            actionbar.setTitle(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //监听返回按钮
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.feedback_commit)
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.feedback_commit:
                if(VerifyUtil.isConnect(c)){
                    commitFeedback();
                }else {
                    ToastUtil.showToast(c, "请检查网络设置");
                }
                break;
            default:break;
        }
    }

    /**
     * 提交意见
     */
    public void commitFeedback(){
        String feedback = feedbackContent.getText().toString().trim();

        if(TextUtils.isEmpty(feedback)){
            ToastUtil.showToast(this, "请填写意见");
        }else {
            FeedBack feedBack = new FeedBack();
            feedBack.setAuthorPhoneNumber(AuthUtil.getCurrentUser().getMobilePhoneNumber());
            feedBack.setContent(feedbackContent.getText().toString().trim());
            feedBack.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e == null){
                        ToastUtil.showToast(c, "提交成功");
                        feedbackContent.setText("");
                    }else {
                        ToastUtil.showToast(c, "提交失败");
                        LogUtil.e("提交失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                    }
                }
            });
        }
    }
}
