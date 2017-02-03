package com.yyp.socket.thread;

import android.os.Handler;
import android.os.Message;

import com.yyp.socket.bean.header.PktHeader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by yyp on 2016/12/13.
 */

public class SocketThread extends Thread {

    private static final String addr = "117.23.249.158";
//    private static final String addr = "10.0.2.2";
    private static final int port = 8888;
    private int timeout = 3000;
    private Socket socket;

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    Handler outHandler;
    Handler inHandler;

    public boolean isRun = true;

    public SocketThread(){

    }

    public SocketThread(Handler outHandler, Handler inHandler){
        this.outHandler = outHandler;
        this.inHandler = inHandler;
    }

    /**
     * 连接
     */
    public void connect(){
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(addr, port), timeout);
            if(socket.isConnected()){
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
            }
        }catch (UnknownHostException e) {
            e.printStackTrace();
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收数据
     */
    @Override
    public void run() {
        connect();
        while(isRun) {
            try {
                if (socket.isConnected()) {
                    //接收头部包长
                    byte[] recvBufHeader = new byte[4];
                    dataInputStream.read(recvBufHeader, 0, 4);
                    PktHeader header = new PktHeader(recvBufHeader);
                    //根据包长接收具体数据
                    int pktLen = header.getPktLen();
                    byte[] recvBuf = new byte[pktLen];
                    dataInputStream.read(recvBuf, 0, pktLen);
                    //合成数据
                    byte[] data = new byte[4+pktLen];
                    System.arraycopy(recvBufHeader, 0, data, 0, 4);
                    System.arraycopy(recvBuf, 0, data, 4, pktLen);

                    Message message = inHandler.obtainMessage();
                    message.what = 1;
                    message.obj = data;
                    inHandler.sendMessage(message);
                } else {
                    connect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送数据
     * @param data
     */
    public void send(byte[] data){
        try {
            if(socket.isConnected()) {
                dataOutputStream.write(data);
                dataOutputStream.flush();

                Message message = outHandler.obtainMessage();
                message.what = 1;
                outHandler.sendMessage(message);
            }else{
                Message message = outHandler.obtainMessage();
                message.what = 0;
                outHandler.sendMessage(message);
            }
        } catch (IOException e) {
            Message message = outHandler.obtainMessage();
            message.what = 0;
            outHandler.sendMessage(message);
            e.printStackTrace();
        }
    }

    /**
     * 关闭资源
     */
    public void close(){
        try{
            if(socket != null){
                isRun = false;
                dataOutputStream.close();
                dataInputStream.close();
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
