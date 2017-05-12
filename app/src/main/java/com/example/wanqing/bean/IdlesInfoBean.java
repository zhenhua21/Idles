package com.example.wanqing.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by dahuahua on 2017/4/2.
 */

public class IdlesInfoBean extends BmobObject {
    private String user_id;
    private String classify;
    private String idle_name;
    private String content;
    private String phone;
    private int price;
    private int likes;
    private List<String> pictures;
    private int isSold;     //0表示没有卖出，1表示卖出

    public void setUser_id(String id) {
        this.user_id = id;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setClassify(String str) {
        this.classify = str;
    }

    public String getClassify() {
        return this.classify;
    }

    public void setIdle_name(String name) {
        this.idle_name = name;
    }

    public String getIdle_name() {
        return this.idle_name;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public String getContent() {
        return this.content;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPrice(int i) {
        this.price = i;
    }

    public int getPrice() {
        return this.price;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLikes() {
        return this.likes;
    }

    public void setPictures(List<String> list) {
        this.pictures = list;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setIsSold(int i) {
        this.isSold = i;
    }

    public int getIsSold() {
        return this.isSold;
    }

}
