package com.yyp.socket.config;

/**
 * Created by yyp on 2016/12/1.
 */

public class PktType {

    public static final int PKT_LOGIN = 0;
    public static final int PKT_LOGOUT = 1;
    public static final int PKT_PLAY_START_STOP = 2;
    public static final int PKT_PLAY_START_STOP_REPLY = 3;
    public static final int PKT_LOGIN_REPLY = 4;
    public static final int PKT_GET_MUSIC_LIST = 5;
    public static final int PKT_GET_MUSIC_LIST_REPLY = 6;

    public static final int LOGIN_FAIL = 0;
    public static final int LOGIN_SUCCESS = 1;

    public static final int PLAY_START = 0;
    public static final int PLAY_STOP = 1;

    public static final int PLAY_START_FAIL = 0;
    public static final int PLAY_START_SUCCESS = 1;
    public static final int PLAY_STOP_FAIL = 2;
    public static final int PLAY_STOP_SUCCESS = 3;
}
