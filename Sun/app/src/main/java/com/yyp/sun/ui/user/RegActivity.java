package com.yyp.sun.ui.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yyp.sun.R;
import com.yyp.sun.config.SunInfo;
import com.yyp.sun.ui.user.data.UserInfo;
import com.yyp.sun.util.ImageUtil;
import com.yyp.sun.util.LogUtil;
import com.yyp.sun.util.ToastUtil;
import com.yyp.sun.util.VerifyUtil;
import com.yyp.sun.view.LoadingDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RegActivity extends AppCompatActivity {

    ActionBar actionbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.reg_account)
    EditText regAccount;
    @BindView(R.id.reg_password)
    EditText regPassword;
    @BindView(R.id.reg_confirm_password)
    EditText regConfirmPassword;
    @BindView(R.id.reg_header_view)
    SimpleDraweeView headerView;

    Context c;
    LoadingDialog loadingDialog;
    // 是否已保存头像
    boolean isSaveImage = false;

    private String TAG = "RegActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        ButterKnife.bind(this);
        c = this;

        initToolbar();
        initHeaderView();
        initLoadDialog();
    }

    /**
     * 初始化头部
     */
    public void initToolbar() {
        // ToolBar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        // 设置标题
        toolbar_title.setText(R.string.reg_title);
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
     * 初始化上传头像的图片
     */
    public void initHeaderView(){
        headerView.setImageURI(Uri.parse("res://com.yyp.sun/" + R.drawable.upload_image));
    }

    /**
     * 初始化 LoadDialog
     */
    public void initLoadDialog(){
        loadingDialog = new LoadingDialog(c, R.style.loading_dialog);
        // 不能自己取消
        loadingDialog.setCancelable(false);
        loadingDialog.initDialog("注册中...");
    }

    /**
     * 点击监听
     * @param v
     */
    @OnClick({R.id.reg_singup, R.id.reg_header_view})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_singup:
                if(VerifyUtil.isConnect(c)){
                    signUp();
                }else {
                    ToastUtil.showToast(c, "请检查网络设置");
                }
                break;
            case R.id.reg_header_view:
                uploadHeaderView();
                break;
            default:
                break;
        }
    }

    /**
     * 上传头像
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

    /**
     * 注册
     */
    public void signUp() {
        String account = regAccount.getText().toString().trim();
        String password = regPassword.getText().toString().trim();
        String confirmPsd = regConfirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(account) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPsd)){
            ToastUtil.showToast(c, "请仔细填写");
        }else{
            if (!VerifyUtil.isMobile(account)){
                ToastUtil.showToast(c, "手机号无效");
            }else {
                if (!password.equals(confirmPsd)){
                    ToastUtil.showToast(c, "密码不一致");
                }else{
                    if(password.length() < 8){
                        ToastUtil.showToast(c, "密码不安全");
                    }else {
                        if(!isSaveImage){
                            ToastUtil.showToast(c, "请重新选取头像");
                        }else{
                            // 显示加载圈
                            loadingDialog.show();

                            final UserInfo user = new UserInfo();
                            user.setUsername(account);
                            user.setPassword(password);
                            user.setSex("男");
                            user.setMobilePhoneNumber(account);
                            user.setMobilePhoneNumberVerified(true);
                            // 上传头像
                            final BmobFile bmobFile = new BmobFile(new File(SunInfo.BASE_FILE_URL+SunInfo.HEAD_IMAGE_URL+SunInfo.HEAD_IMAGE_NAME));
                            bmobFile.uploadblock(new UploadFileListener() {

                                @Override
                                public void done(BmobException e) {
                                    if(e == null){
                                        ToastUtil.showToast(c, "头像上传成功");
                                        // bmobFile.getFileUrl()--返回的上传文件的完整地址
                                        user.setAvatarUrl(bmobFile.getFileUrl());
                                        user.setAvatarName(bmobFile.getFilename());
                                        // 注册
                                        user.signUp(new SaveListener<UserInfo>() {
                                            @Override
                                            public void done(UserInfo userInfo, BmobException e) {
                                                if(e == null){
                                                    ToastUtil.showToast(c, "注册成功");
                                                    Intent goLogin = new Intent(c, LoginActivity.class);
                                                    startActivity(goLogin);
                                                    finish();
                                                }else {
                                                    loadingDialog.dismiss();
                                                    ToastUtil.showToast(c, "注册失败");
                                                    LogUtil.e("注册失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                                                }
                                            }
                                        });
                                    }else{
                                        loadingDialog.dismiss();
                                        ToastUtil.showToast(c, "头像上传失败");
                                        LogUtil.e("头像上传失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                                    }
                                }

                                @Override
                                public void onProgress(Integer value) {
                                    // 返回的上传进度（百分比）
                                }
                            });
                        }
                    }
                }
            }
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
                        headerView.setImageURI(uri);
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog != null){
            loadingDialog.stopRotateLoading();
            loadingDialog.dismiss();
        }
    }
}
