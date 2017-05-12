package com.example.wanqing.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by dahuahua on 2017/5/11.
 */

public class DealBean extends BmobObject {
    private String user;
    private String business;
    private IdlesInfoBean idle;
    private String picture;
    private String content;


    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return this.user;
    }

    public void setBusiness(String user) {
        this.business = user;
    }

    public String getBusiness() {
        return this.business;
    }

    public void setIdle(IdlesInfoBean idle) {
        this.idle = idle;
    }

    public IdlesInfoBean getIdle() {
        return this.idle;
    }

    public void setPicture(String s) {
        this.picture = s;
    }
    public String getPicture() {
        return this.picture;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

}
