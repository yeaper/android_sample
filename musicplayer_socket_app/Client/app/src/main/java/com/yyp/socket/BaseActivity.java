package com.yyp.socket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by yyp on 2016/12/9.
 */
public class BaseActivity extends AppCompatActivity {
    public static final String addr = "192.168.0.114";
    public static final int port = 8888;

    public Socket socket;
    public SocketAddress address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSocket();
    }

    public void initSocket(){
        socket = new Socket();
        address = new InetSocketAddress(addr, port);
    }
}
