package com.yyp.socket.bean.music;

/**
 * Created by yyp on 2016/12/10.
 */

public class Music {
    private String musicName;

    public Music(){

    }

    public Music(String musicName){
        this.musicName = musicName;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }
}
