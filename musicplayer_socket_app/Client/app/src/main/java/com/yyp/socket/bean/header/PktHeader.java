package com.yyp.socket.bean.header;

import com.yyp.socket.utils.NetDataTransformUtils;

/**
 * Created by yyp on 2016/12/1.
 */

public class PktHeader{
    private int pktLen;
    private int pktType;

    public PktHeader(){

    }

    public PktHeader(byte[] b){
        if(b.length == 4){
            byte[] b1 = new byte[4];
            int i;
            for (i=0;i<4;i++) {
                b1[i] = b[i];
            }
            this.pktLen = NetDataTransformUtils.byteArrayToInt(b1);
        }else if(b.length >= 8){
            byte[] b1 = new byte[4];
            byte[] b2 = new byte[4];
            int i;
            for (i=0;i<4;i++) {
                b1[i] = b[i];
            }
            this.pktLen = NetDataTransformUtils.byteArrayToInt(b1);
            for (i=0;i<4;i++) {
                b2[i] = b[4+i];
            }
            this.pktType = NetDataTransformUtils.byteArrayToInt(b2);
        }

    }

    public PktHeader(int pktLen, int pktType){
        this.pktLen = pktLen;
        this.pktType = pktType;
    }

    public int getPktLen() {
        return pktLen;
    }

    public void setPktLen(int pktLen) {
        this.pktLen = pktLen;
    }

    public int getPktType() {
        return pktType;
    }

    public void setPktType(int pktType) {
        this.pktType = pktType;
    }
}
