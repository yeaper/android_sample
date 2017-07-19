package com.xh.sun.ui.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xh.sun.R;
import com.xh.sun.config.SunInfo;
import com.xh.sun.ui.user.data.UserInfo;
import com.xh.sun.util.AuthUtil;
import com.xh.sun.util.ImageUtil;
import com.xh.sun.util.LogUtil;
import com.xh.sun.util.ToastUtil;
import com.xh.sun.util.VerifyUtil;
import com.xh.sun.view.LoadingDialog;
import com.xh.sun.view.SexSelectDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ProfileActivity extends AppCompatActivity {

    ActionBar actionbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.profile_header_view)
    SimpleDraweeView profileHeaderView;
    @BindView(R.id.profile_nickname)
    EditText profileNickname;
    @BindView(R.id.profile_sex)
    TextView profileSex;
    @BindView(R.id.profile_phone_number)
    TextView profilePhoneNumber;

    Context c;
    LoadingDialog loadingDialog;
    UserInfo user;
    // 是否已保存头像
    boolean isSaveImage = false;

    // 性别选择view
    SexSelectDialog sexSelectDialog;
    RadioGroup rg;
    TextView sex_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        c = ProfileActivity.this;
        user = AuthUtil.getCurrentUser();

        initToolbar();
        initProfile();
        initLoadDialog();
    }

    /**
     * 初始化头部
     */
    public void initToolbar() {
        // ToolBar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        // 设置标题
        toolbar_title.setText(R.string.profile_title);
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
     * 初始化个人信息
     */
    public void initProfile(){
        profileHeaderView.setImageURI(user.getAvatarUrl());
        profileNickname.setText(user.getNickName());
        profileSex.setText(user.getSex());
        profilePhoneNumber.setText(user.getMobilePhoneNumber());
    }

    /**
     * 初始化 LoadDialog
     */
    public void initLoadDialog(){
        loadingDialog = new LoadingDialog(c, R.style.loading_dialog);
        // 不能自己取消
        loadingDialog.setCancelable(false);
        loadingDialog.initDialog("保存中...");
    }

    /**
     * 选择性别
     */
    public void sexSelect(){
        sexSelectDialog = new SexSelectDialog(c);
        sexSelectDialog.show();
        rg = (RadioGroup) sexSelectDialog.getCustomView().findViewById(R.id.profile_rg);
        sex_ok = (TextView) sexSelectDialog.getCustomView().findViewById(R.id.profile_sex_ok);
        sex_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rg.getCheckedRadioButtonId()) {
                    case R.id.profile_rg_boy:
                        profileSex.setText("男");
                        break;
                    case R.id.profile_rg_girl:
                        profileSex.setText("女");
                        break;
                    default:break;
                }
                sexSelectDialog.dismiss();
            }
        });
    }

    /**
     * 选择要上传的头像
     */
    private void uploadHeaderView() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        // 进入相册选择图片
        startActivityForResult(intentFromGallery, SunInfo.CODE_GALLERY_REQUEST);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 127);
        intent.putExtra("outputY", 127);
        intent.putExtra("return-data", true);
        // 进入编辑器剪裁图片
        startActivityForResult(intent, SunInfo.CODE_RESULT_REQUEST);
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
                if(VerifyUtil.isConnect(c)){
                    saveProfile();
                }else{
                    ToastUtil.showToast(c, "请检查网络设置");
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.profile_header_rl, R.id.profile_sex_rl, R.id.profile_rstpsd_rl, R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_header_rl:
                uploadHeaderView();
                break;
            case R.id.profile_sex_rl:
                sexSelect();
                break;
            case R.id.profile_rstpsd_rl:
                Intent goRstPsd = new Intent(c, ResetPasswordActivity.class);
                startActivity(goRstPsd);
                finish();
                break;
            case R.id.logout:
                // 退出登录
                BmobUser.logOut();
                Intent goLogin = new Intent(c, LoginActivity.class);
                startActivity(goLogin);
                finish();
                break;
            default:break;
        }
    }

    /**
     * 保存修改的信息
     */
    public void saveProfile(){
        final String nickname = profileNickname.getText().toString().trim();
        String sex = profileSex.getText().toString().trim();

        if(TextUtils.isEmpty(nickname)){
            ToastUtil.showToast(c, "请输入昵称");
        }else {

            loadingDialog.show();

            if(!isSaveImage){
                // 没有改头像
                UserInfo newUser = new UserInfo();
                newUser.setNickName(nickname);
                newUser.setSex(sex);
                // 更新信息
                newUser.update(user.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            backData();
                        }else{
                            loadingDialog.dismiss();
                            ToastUtil.showToast(c, "修改失败");
                            LogUtil.e("修改失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                        }
                    }
                });
            }else{
                // 改头像了，先删除原有头像
                BmobFile file = new BmobFile();
                file.setUrl(user.getAvatarUrl());
                file.delete(new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            LogUtil.i("头像删除成功");
                        }else{
                            LogUtil.e("头像删除失败："+e.getErrorCode()+","+e.getMessage());
                        }
                    }
                });

                final UserInfo newUser = new UserInfo();
                newUser.setNickName(nickname);
                newUser.setSex(sex);
                // 上传头像
                final BmobFile bmobFile = new BmobFile(new File(SunInfo.BASE_FILE_URL+SunInfo.HEAD_IMAGE_URL+SunInfo.HEAD_IMAGE_NAME));
                bmobFile.uploadblock(new UploadFileListener(){

                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            newUser.setAvatarUrl(bmobFile.getFileUrl());
                            newUser.setAvatarName(bmobFile.getFilename());
                            // 更新信息
                            newUser.update(user.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e == null){
                                        backData();
                                    }else{
                                        loadingDialog.dismiss();
                                        ToastUtil.showToast(c, "修改失败");
                                        LogUtil.e("修改失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                                    }
                                }
                            });
                        }else {
                            loadingDialog.dismiss();
                            ToastUtil.showToast(c, "头像上传失败");
                            LogUtil.e("头像上传失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                        }
                    }
                });
            }
        }
    }

    /**
     * 修改完返回刷新请求
     */
    public void backData(){
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isUpdated", true);
        intent.putExtras(bundle);
        setResult(SunInfo.CODE_IN_PROFILE, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 用户没有进行有效的操作，直接返回
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case SunInfo.CODE_GALLERY_REQUEST:
                // 剪裁图片
                cropRawPhoto(intent.getData());
                break;
            case SunInfo.CODE_RESULT_REQUEST:
                if (intent != null) {
                    // 图片拿到后，先保存到本地，再进行设置
                    Bitmap bitmap = intent.getExtras().getParcelable("data");
                    Uri uri = ImageUtil.saveImage(bitmap, SunInfo.HEAD_IMAGE_NAME, SunInfo.BASE_FILE_URL+SunInfo.HEAD_IMAGE_URL);
                    // 判断是否保存了头像
                    if(uri == null){
                        isSaveImage = false;
                    }else{
                        isSaveImage = true;
                        profileHeaderView.setImageURI(uri);
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sexSelectDialog != null){
            sexSelectDialog.dismiss();
        }
        if(loadingDialog != null){
            loadingDialog.stopRotateLoading();
            loadingDialog.dismiss();
        }
    }
}
