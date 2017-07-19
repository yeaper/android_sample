package com.xh.sun.ui.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xh.sun.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppAboutActivity extends AppCompatActivity {

    ActionBar actionbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_about);
        ButterKnife.bind(this);

        initToolbar();
    }

    /**
     * 初始化头部
     */
    public void initToolbar() {
        // ToolBar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        // 设置标题
        toolbar_title.setText(R.string.navigation_app_about);
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

    @OnClick({R.id.app_about_feedback, R.id.app_about_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_about_feedback:
                Intent goFeedBack = new Intent(this, FeedBackActivity.class);
                startActivity(goFeedBack);
                break;
            case R.id.app_about_version:
                break;
        }
    }
}
