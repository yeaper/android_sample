package com.xh.sun.ui.mood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xh.sun.R;
import com.xh.sun.config.SunInfo;
import com.xh.sun.dao.MoodDiaryDataDao;
import com.xh.sun.db.DB;
import com.xh.sun.entity.MoodDiaryData;
import com.xh.sun.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoodDetailActivity extends AppCompatActivity {

    ActionBar actionbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    Context c;
    MoodDiaryData moodDiaryData;
    MoodDiaryDataDao moodDiaryDataDao;

    @BindView(R.id.mood_detail_content)
    TextView moodDetailContent;
    @BindView(R.id.mood_detail_image1)
    SimpleDraweeView moodDetailImage1;
    @BindView(R.id.mood_detail_image2)
    SimpleDraweeView moodDetailImage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_detail);
        ButterKnife.bind(this);
        c = MoodDetailActivity.this;

        initToolbar();
        getData();
        initDB();
    }

    /**
     * 初始化头部
     */
    public void initToolbar() {
        // ToolBar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        // 设置标题
        toolbar_title.setText(R.string.mood_detail_title);
        setSupportActionBar(toolbar);

        actionbar = getSupportActionBar();
        if (actionbar != null) {
            // 设置返回按钮
            actionbar.setDisplayHomeAsUpEnabled(true);
            // 去掉 ActionBar 自带标题
            actionbar.setTitle(null);
        }
    }

    /**
     * 初始化数据库
     */
    public void initDB(){
        moodDiaryDataDao = DB.getInstance(c, SunInfo.DB_NAME_MOOD_DIARY).getMoodDiaryDataDao();
    }

    /**
     * 获取传进来的数据
     */
    private void getData() {
        if (getIntent() != null) {
            moodDiaryData = (MoodDiaryData) getIntent().getExtras().getSerializable("moodDiary");
            initInfo();
        }
    }

    /**
     * 初始化界面数据
     */
    public void initInfo() {
        // 设置图片
        moodDetailImage1.setImageURI("file://" + moodDiaryData.getImageUrl1());
        moodDetailImage2.setImageURI("file://" + moodDiaryData.getImageUrl2());
        moodDetailContent.setText(moodDiaryData.getContent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //监听返回按钮
                finish();
                break;
            case R.id.menu_delete:
                deleteMoodDiary();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 删除当前日记
     */
    public void deleteMoodDiary() {
        moodDiaryDataDao.delete(moodDiaryData);
        ToastUtil.showToast(c, "删除成功");
        backData();
    }

    /**
     * 删除完返回刷新请求
     */
    public void backData(){
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isUpdated", true);
        intent.putExtras(bundle);
        setResult(SunInfo.CODE_IN_MOOD_DETAIL, intent);
        finish();
    }
}
