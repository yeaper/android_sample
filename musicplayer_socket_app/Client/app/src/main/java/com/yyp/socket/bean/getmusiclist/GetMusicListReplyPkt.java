package com.yyp.socket.bean.getmusiclist;

import com.yyp.socket.bean.header.PktHeader;
import com.yyp.socket.utils.NetDataTransformUtils;

/**
 * Created by yyp on 2016/12/10.
 */

public class GetMusicListReplyPkt {
    private PktHeader header;
    private String musicName;

    public GetMusicListReplyPkt(){

    }

    public GetMusicListReplyPkt(byte[] b){
        byte[] b1 = new byte[4];
        byte[] b2 = new byte[4];
        byte[] b3 = new byte[30];
        int i,j;
        for (i=0;i<4;i++) {
            b1[i] = b[i];
        }
        for (i=0;i<4;i++) {
            b2[i] = b[4+i];
        }
        for (i=0,j=8;j<38;i++,j++) {
            b3[i] = b[j];
        }
        this.header = new PktHeader(NetDataTransformUtils.byteArrayToInt(b1), NetDataTransformUtils.byteArrayToInt(b2));
        this.musicName = NetDataTransformUtils.byteArrayToString(b3, 30);
    }

    public PktHeader getHeader() {
        return header;
    }

    public void setHeader(PktHeader header) {
        this.header = header;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }
}
