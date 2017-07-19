package com.xh.sun.ui.app.data;

import cn.bmob.v3.BmobObject;

/**
 * 意见反馈
 */
public class FeedBack extends BmobObject {

    private String authorPhoneNumber; // 反馈者手机号
    private String content; // 反馈内容

    protected long serialVersionUID = 1L; //对象序列化匹配的标志

    public String getAuthorPhoneNumber() {
        return authorPhoneNumber;
    }

    public void setAuthorPhoneNumber(String authorPhoneNumber) {
        this.authorPhoneNumber = authorPhoneNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
