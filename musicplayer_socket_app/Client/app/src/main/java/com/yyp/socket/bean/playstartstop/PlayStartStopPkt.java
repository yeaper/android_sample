package com.yyp.socket.bean.playstartstop;

import com.yyp.socket.bean.header.PktHeader;
import com.yyp.socket.utils.NetDataTransformUtils;

/**
 * Created by yyp on 2016/12/9.
 */

public class PlayStartStopPkt {
    private PktHeader header;
    private int type;
    private String musicName;

    public PlayStartStopPkt(){

    }

    public PlayStartStopPkt(PktHeader header, int type, String musicName){
        this.header = header;
        this.type = type;
        this.musicName = musicName;
    }

    public PktHeader getHeader() {
        return header;
    }

    public void setHeader(PktHeader header) {
        this.header = header;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public byte[] getBuf() {
        byte[] buf = new byte[4+this.getHeader().getPktLen()];
        byte[] temp = NetDataTransformUtils.intToByteArray(this.getHeader().getPktLen());
        System.arraycopy(temp, 0, buf, 0, 4);

        temp = NetDataTransformUtils.intToByteArray(this.getHeader().getPktType());
        System.arraycopy(temp, 0, buf, 4, 4);

        temp = NetDataTransformUtils.intToByteArray(this.getType());
        System.arraycopy(temp, 0, buf, 8, 4);

        byte[] temp2 = NetDataTransformUtils.stringToByteArray(this.getMusicName());
        System.arraycopy(temp2, 0, buf, 12, temp2.length);

        return buf;
    }
}
