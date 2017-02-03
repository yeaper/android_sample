package com.yyp.socket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.yyp.socket.bean.header.PktHeader;
import com.yyp.socket.bean.login.LoginPkt;
import com.yyp.socket.bean.login.LoginReplyPkt;
import com.yyp.socket.config.PktType;
import com.yyp.socket.thread.SocketThread;
import com.yyp.socket.utils.ToastUtils;
import com.yyp.socket.view.MusicPlayActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.main_username)
    EditText mainUsername;
    @BindView(R.id.main_password)
    EditText mainPassword;

    ProgressDialog progressDialog;

    SocketThread loginThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        startSocket();
        progressDialog = new ProgressDialog(MainActivity.this);
    }

    public Handler outHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    ToastUtils.showToast(MainActivity.this, "网络错误");
                    break;
                case 1:
                    progressDialog.setMessage("正在登录...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    break;
            }
        }
    };

    public Handler inHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    LoginReplyPkt loginReplyPkt = new LoginReplyPkt((byte[]) msg.obj);
                    if (loginReplyPkt.getHeader().getPktType() == PktType.PKT_LOGIN_REPLY && loginReplyPkt.getRet() == PktType.LOGIN_SUCCESS) {
                        progressDialog.dismiss();
                        ToastUtils.showToast(MainActivity.this, "登录成功");
                        loginThread.isRun = false;
                        // 登录成功跳转
                        Bundle bundle = new Bundle();
                        bundle.putString("username",mainUsername.getText().toString().trim());
                        Intent goMusicPlay = new Intent(getApplicationContext(), MusicPlayActivity.class);
                        goMusicPlay.putExtras(bundle);
                        startActivity(goMusicPlay);
                        finish();
                    } else {
                        progressDialog.dismiss();
                        ToastUtils.showToast(MainActivity.this, "登录失败");
                    }
                    break;
            }

            super.handleMessage(msg);
        }
    };

    public void startSocket(){
        loginThread = new SocketThread(outHandler, inHandler);
        loginThread.start();
    }

    public void login() {
        String usernameStr = mainUsername.getText().toString().trim();
        String passwordStr = mainPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(usernameStr) && !TextUtils.isEmpty(passwordStr)) {
            //构建登录包
            LoginPkt loginPkt = new LoginPkt();
            if (usernameStr.length() < 20) {
                for (int i = usernameStr.length(); i < 20; i++) {
                    usernameStr += '\0';
                }
            }
            loginPkt.setUsername(usernameStr);
            loginPkt.setPassword(passwordStr);
            loginPkt.setHeader(new PktHeader(4+20+20, PktType.PKT_LOGIN));

            loginThread.send(loginPkt.getBuf());
        }
    }

    @OnClick(R.id.main_login)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_login:
                login();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
        super.onDestroy();
    }
}
