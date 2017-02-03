package com.yyp.socket.bean.login;

import com.yyp.socket.bean.header.PktHeader;
import com.yyp.socket.utils.NetDataTransformUtils;

/**
 * Created by yyp on 2016/12/1.
 */

public class LoginReplyPkt {
    PktHeader header;
    int ret;

    public LoginReplyPkt(){

    }

    public LoginReplyPkt(byte[] b){
        byte[] b1 = new byte[4];
        byte[] b2 = new byte[4];
        byte[] b3 = new byte[4];
        int i;
        for (i=0;i<4;i++) {
            b1[i] = b[i];
        }
        for (i=0;i<4;i++) {
            b2[i] = b[4+i];
        }
        for (i=0;i<4;i++) {
            b3[i] = b[8+i];
        }
        this.header = new PktHeader(NetDataTransformUtils.byteArrayToInt(b1), NetDataTransformUtils.byteArrayToInt(b2));
        this.ret = NetDataTransformUtils.byteArrayToInt(b3);
    }

    public PktHeader getHeader() {
        return header;
    }

    public void setHeader(PktHeader header) {
        this.header = header;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }
}
