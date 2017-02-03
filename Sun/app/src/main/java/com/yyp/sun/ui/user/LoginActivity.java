package com.yyp.sun.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.yyp.sun.MainActivity;
import com.yyp.sun.R;
import com.yyp.sun.ui.user.data.UserInfo;
import com.yyp.sun.util.AuthUtil;
import com.yyp.sun.util.LogUtil;
import com.yyp.sun.util.ToastUtil;
import com.yyp.sun.util.VerifyUtil;
import com.yyp.sun.view.LoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends Activity {

    @BindView(R.id.login_account)
    EditText loginAccount;
    @BindView(R.id.login_password)
    EditText loginPassword;

    Context c;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        c = LoginActivity.this;

        initLoadDialog();
        rememberUser();
    }

    /**
     * 初始化 LoadDialog
     */
    public void initLoadDialog(){
        loadingDialog = new LoadingDialog(c, R.style.loading_dialog);
        // 不能自己取消
        loadingDialog.setCancelable(false);
        loadingDialog.initDialog("登录中...");
    }

    /**
     * 记住用户信息
     */
    public void rememberUser(){
        loginAccount.setText(AuthUtil.getAccount());
        loginPassword.setText(AuthUtil.getPassword());
    }

    @OnClick({R.id.login_login, R.id.loginToReg, R.id.loginToRst})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_login:
                if(VerifyUtil.isConnect(c)){
                    login();
                }else{
                    ToastUtil.showToast(c, "请检查网络设置");
                }
                break;
            case R.id.loginToReg:
                Intent goReg = new Intent(getApplicationContext(), RegActivity.class);
                startActivity(goReg);
                break;
            case R.id.loginToRst:
                Intent goRst = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(goRst);
                break;
            default:break;
        }
    }

    /**
     * 登录
     */
    public void login(){
        final String account = loginAccount.getText().toString().trim();
        final String password = loginPassword.getText().toString().trim();

        if(TextUtils.isEmpty(account) || TextUtils.isEmpty(password)){
            ToastUtil.showToast(c, "请仔细填写");
        }else {

            loadingDialog.show();

            UserInfo user = new UserInfo();
            user.setUsername(account);
            user.setPassword(password);
            user.login(new SaveListener<UserInfo>() {
                @Override
                public void done(UserInfo userInfo, BmobException e) {
                    if(e == null){
                        // 登录成功后，保存用户信息
                        AuthUtil.putAccount(account);
                        AuthUtil.putPassword(password);

                        Intent goMain = new Intent(c, MainActivity.class);
                        startActivity(goMain);
                        finish();
                    }else{
                        loadingDialog.dismiss();
                        ToastUtil.showToast(c, "登录失败");
                        LogUtil.e("登录失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                    }
                }
            });
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
