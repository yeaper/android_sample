package com.yyp.socket.bean.logout;

import com.yyp.socket.bean.header.PktHeader;
import com.yyp.socket.utils.NetDataTransformUtils;

/**
 * Created by yyp on 2016/12/1.
 */

public class LogoutPkt {
    private PktHeader header;
    private String username;

    public LogoutPkt(){

    }

    public LogoutPkt(PktHeader header, String username){
        this.header = header;
        this.username = username;
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

    public byte[] getBuf() {
        byte[] buf = new byte[4+this.getHeader().getPktLen()];
        byte[] temp = NetDataTransformUtils.intToByteArray(this.getHeader().getPktLen());
        System.arraycopy(temp, 0, buf, 0, 4);

        temp = NetDataTransformUtils.intToByteArray(this.getHeader().getPktType());
        System.arraycopy(temp, 0, buf, 4, 4);

        temp = NetDataTransformUtils.stringToByteArray(this.getUsername());
        System.arraycopy(temp, 0, buf, 8, temp.length);

        return buf;
    }
}
