package com.yyp.sun.ui.mood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;
import com.yyp.sun.R;
import com.yyp.sun.config.SunInfo;
import com.yyp.sun.dao.MoodDiaryDataDao;
import com.yyp.sun.db.DB;
import com.yyp.sun.entity.MoodDiaryData;
import com.yyp.sun.util.AuthUtil;
import com.yyp.sun.util.TimeUtil;
import com.yyp.sun.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublishMoodActivity extends AppCompatActivity {

    ActionBar actionbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.publish_mood_content)
    EditText publishMoodContent;
    @BindView(R.id.publish_mood_image1)
    SimpleDraweeView publishMoodImage1;
    @BindView(R.id.publish_mood_image2)
    SimpleDraweeView publishMoodImage2;

    Context c;
    List<String> imageStrings;

    private MoodDiaryDataDao moodDiaryDataDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_mood);
        ButterKnife.bind(this);
        c = PublishMoodActivity.this;

        initToolbar();
        initImage();
        initDB();
    }

    /**
     * 初始化头部
     */
    public void initToolbar() {
        // ToolBar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        // 设置标题
        toolbar_title.setText(R.string.publish_mood_title);
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
     * 初始化上传照片
     */
    public void initImage(){
        imageStrings = new ArrayList<>();
        publishMoodImage1.setImageURI(Uri.parse("res://com.yyp.sun/" + R.drawable.upload_image));
    }

    /**
     * 初始化数据库
     */
    public void initDB(){
        moodDiaryDataDao = DB.getInstance(c, SunInfo.DB_NAME_MOOD_DIARY).getMoodDiaryDataDao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //监听返回按钮
                finish();
                break;
            case R.id.menu_save:
                saveMoodDiary();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.publish_mood_image1, R.id.publish_mood_image2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.publish_mood_image1:
                uploadImage();
                break;
            case R.id.publish_mood_image2:
                if(publishMoodImage2.getVisibility() == View.VISIBLE){
                    uploadImage();
                }
                break;
        }
    }

    /**
     * 选择多个图片
     */
    private void uploadImage() {
        // 自定义图片加载器
        ImageLoader loader = new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                // TODO 在这边可以自定义图片加载库来加载ImageView，例如Glide、Picasso、ImageLoader等
                Picasso.with(context).load("file://" + path).into(imageView);
            }
        };
        // 自由配置选项
        ImgSelConfig config = new ImgSelConfig.Builder(loader)
                // 是否多选
                .multiSelect(true)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.parseColor("#ffffff"))
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.drawable.arrow_left)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#bb66ee"))
                // 裁剪大小。needCrop为true的时候配置
                .cropSize(1, 1, 200, 200)
                .needCrop(true)
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(2)
                .build();

        // 跳转到图片选择器
        ImgSelActivity.startActivity(this, config, SunInfo.CODE_MORE_RESULT_REQUEST);
    }

    /**
     * 保存这篇随笔
     */
    public void saveMoodDiary(){
        String content = publishMoodContent.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            ToastUtil.showToast(c, "内容不能为空");
        }else {
            MoodDiaryData moodDiaryData = new MoodDiaryData();
            moodDiaryData.setContent(content);
            moodDiaryData.setAuthorID(AuthUtil.getCurrentUser().getObjectId());
            moodDiaryData.setCreateDate(TimeUtil.getSystemTime());
            // 需要分情况保存图片URL
            switch (imageStrings.size()){
                case 0:
                    moodDiaryData.setImageUrl1("");
                    moodDiaryData.setImageUrl2("");
                    break;
                case 1:
                    moodDiaryData.setImageUrl1(imageStrings.get(0));
                    moodDiaryData.setImageUrl2("");
                    break;
                case 2:
                    moodDiaryData.setImageUrl1(imageStrings.get(0));
                    moodDiaryData.setImageUrl2(imageStrings.get(1));
                    break;
                default:break;
            }
            moodDiaryDataDao.insert(moodDiaryData);
            ToastUtil.showToast(c, "保存成功");
            backData();
        }
    }

    /**
     * 发布完返回刷新请求
     */
    public void backData(){
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isUpdated", true);
        intent.putExtras(bundle);
        setResult(SunInfo.CODE_IN_PUBLISH_MOOD, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == SunInfo.CODE_MORE_RESULT_REQUEST && resultCode == RESULT_OK && data != null) {
            imageStrings = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            switch (imageStrings.size()){
                case 1:
                    publishMoodImage1.setImageURI(Uri.parse("file://" + imageStrings.get(0)));
                    publishMoodImage2.setImageURI(Uri.parse("res://com.yyp.sun/" + R.drawable.upload_image));
                    publishMoodImage2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    publishMoodImage1.setImageURI(Uri.parse("file://" + imageStrings.get(0)));
                    publishMoodImage2.setImageURI(Uri.parse("file://" + imageStrings.get(1)));
                    publishMoodImage2.setVisibility(View.VISIBLE);
                    break;
                default:break;
            }
        }
    }
}
