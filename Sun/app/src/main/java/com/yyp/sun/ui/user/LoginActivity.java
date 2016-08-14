package com.yyp.sun.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yyp.sun.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    @BindView(R.id.login_account)
    EditText loginAccount;
    @BindView(R.id.login_password)
    EditText loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login_login, R.id.loginToReg, R.id.loginToRst})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_login:
                login();
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

    }
}
