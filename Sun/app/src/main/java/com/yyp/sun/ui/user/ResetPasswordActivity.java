package com.yyp.sun.ui.user;

import android.content.Context;
import android.content.Intent;
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
import com.yyp.sun.util.CountDownTimerUtils;
import com.yyp.sun.util.LogUtil;
import com.yyp.sun.util.ToastUtil;
import com.yyp.sun.util.VerifyUtil;
import com.yyp.sun.view.LoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ResetPasswordActivity extends AppCompatActivity {

    ActionBar actionbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    CountDownTimerUtils countDownTimer;
    @BindView(R.id.rst_send_code)
    TextView sendCode;
    @BindView(R.id.rst_phone_number)
    EditText rstPhoneNumber;
    @BindView(R.id.rst_verify_code)
    EditText rstVerifyCode;
    @BindView(R.id.rst_new_password)
    EditText rstNewPassword;
    @BindView(R.id.rst_confirm_password)
    EditText rstConfirmPassword;

    Context c;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        c = ResetPasswordActivity.this;

        initToolbar();
        initLoadDialog();
    }

    /**
     * 初始化头部
     */
    public void initToolbar() {
        // ToolBar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        // 设置标题3
        toolbar_title.setText(R.string.rst_title);
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
     * 初始化 LoadDialog
     */
    public void initLoadDialog(){
        loadingDialog = new LoadingDialog(c, R.style.loading_dialog);
        // 不能自己取消
        loadingDialog.setCancelable(false);
        loadingDialog.initDialog("修改中...");
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

    @OnClick({R.id.rst_send_code, R.id.rst_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rst_send_code:
                if(VerifyUtil.isConnect(c)){
                    getVerifyCode();
                }else{
                    ToastUtil.showToast(c, "请检查网络设置");
                }
                break;
            case R.id.rst_confirm:
                if(VerifyUtil.isConnect(c)){
                    rstPassword();
                }else{
                    ToastUtil.showToast(c, "请检查网络设置");
                }
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    public void getVerifyCode(){
        String phoneNumber = rstPhoneNumber.getText().toString().trim();

        BmobSMS.requestSMSCode(phoneNumber, "重置密码", new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId,BmobException e) {
                if(e == null){
                    //验证码发送成功
                    ToastUtil.showToast(c, "验证码已发送");
                    // 发送成功进入倒计时
                    countDownTimer = new CountDownTimerUtils(sendCode, 60000, 1000);
                    countDownTimer.start();
                }else{
                    LogUtil.e("验证码获取失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                }
            }
        });
    }

    /**
     * 修改密码
     */
    public void rstPassword(){
        String verifyCode = rstVerifyCode.getText().toString().trim();
        String newPassword = rstNewPassword.getText().toString().trim();
        String confirmPassword = rstConfirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(verifyCode) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)){
            ToastUtil.showToast(c, "请仔细填写");
        }else {
            if(verifyCode.length() < 6){
                ToastUtil.showToast(c, "请检查验证码");
            }else{
                if (!newPassword.equals(confirmPassword)){
                    ToastUtil.showToast(c, "密码不一致");
                }else {
                    if (newPassword.length() < 8) {
                        ToastUtil.showToast(c, "密码不安全");
                    } else {
                        loadingDialog.show();

                        BmobUser.resetPasswordBySMSCode(verifyCode, newPassword, new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if(e == null){
                                    ToastUtil.showToast(c, "密码修改成功");
                                    Intent goLogin = new Intent(c, LoginActivity.class);
                                    startActivity(goLogin);
                                    finish();
                                }else{
                                    loadingDialog.dismiss();
                                    LogUtil.e("修改失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                                }
                            }
                        });
                    }
                }
            }
        }
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
