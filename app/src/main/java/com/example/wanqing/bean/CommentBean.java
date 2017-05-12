package com.example.wanqing.bean;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by dahuahua on 2017/5/10.
 */

public class CommentBean extends BmobObject{
    private IdlesInfoBean idle;
    private UserBean user;      //评价方（主体）
    private String business_id; //被评价方（商家）
    private String user_img;
    private String user_name;
    private String comment;
    private int type; //0评论\1回复
    private BmobDate date;

    public void setIdle(IdlesInfoBean idle) {
        this.idle = idle;
    }

    public IdlesInfoBean getIdle() {
        return this.idle;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public UserBean getUser() {
        return this.user;
    }

    public void setUser_img(String img) {
        this.user_img = img;
    }

    public String getUser_img() {
        return this.user_img;
    }

    public void setUser_name(String name) {
        this.user_name = name;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setDate(BmobDate date) {
        this.date = date;
    }

    public BmobDate getDate() {
        return this.date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return this.comment;
    }

    public void setType(int i) {
        this.type = i;
    }

    public int getType() {
        return this.type;
    }

    public void setBusiness_id(String id) {
        this.business_id = id;
    }

    public String getBusiness_id() {
        return this.business_id;
    }
}
