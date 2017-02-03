package com.yyp.sun.ui.user.data;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * Created by yyp on 2016/8/12.
 */
public class UserInfo extends BmobUser implements Serializable {

    private String nickName; //用户昵称
    private String avatarName; //用户头像文件名
    private String avatarUrl; //用户头像文件路径
    private String sex; //用户性别

    protected long serialVersionUID = 1L; //对象序列化匹配的标志

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
