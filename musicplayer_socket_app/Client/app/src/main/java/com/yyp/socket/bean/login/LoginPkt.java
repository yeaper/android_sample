package com.yyp.socket.bean.login;

import com.yyp.socket.bean.header.PktHeader;
import com.yyp.socket.utils.NetDataTransformUtils;

/**
 * Created by yyp on 2016/12/1.
 */

public class LoginPkt{
    private PktHeader header;
    private String username;
    private String password;

    public LoginPkt(){

    }

    public LoginPkt(PktHeader header, String username, String password){
        this.header = header;
        this.username = username;
        this.password = password;
    }

    public PktHeader getHeader() {
        return header;
    }

    public void setHeader(PktHeader header) {
        this.header = header;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getBuf() {
        byte[] buf = new byte[4+this.getHeader().getPktLen()];
        byte[] temp = NetDataTransformUtils.intToByteArray(this.getHeader().getPktLen());
        System.arraycopy(temp, 0, buf, 0, 4);

        temp = NetDataTransformUtils.intToByteArray(this.getHeader().getPktType());
        System.arraycopy(temp, 0, buf, 4, 4);

        temp = NetDataTransformUtils.stringToByteArray(this.getUsername());
        System.arraycopy(temp, 0, buf, 8, 20);

        byte[] temp2 = NetDataTransformUtils.stringToByteArray(this.getPassword());
        System.arraycopy(temp2, 0, buf, 28, temp2.length);

        return buf;
    }
}
