package com.yyp.socket.bean.getmusiclist;

import com.yyp.socket.bean.header.PktHeader;
import com.yyp.socket.utils.NetDataTransformUtils;

/**
 * Created by yyp on 2016/12/10.
 */

public class GetMusicListPkt {
    private PktHeader header;

    public GetMusicListPkt(){

    }

    public GetMusicListPkt(PktHeader header){
        this.header = header;
    }

    public PktHeader getHeader() {
        return header;
    }

    public void setHeader(PktHeader header) {
        this.header = header;
    }

    public byte[] getBuf() {
        byte[] buf = new byte[4+this.getHeader().getPktLen()];
        byte[] temp = NetDataTransformUtils.intToByteArray(this.getHeader().getPktLen());
        System.arraycopy(temp, 0, buf, 0, 4);

        temp = NetDataTransformUtils.intToByteArray(this.getHeader().getPktType());
        System.arraycopy(temp, 0, buf, 4, 4);

        return buf;
    }
}
