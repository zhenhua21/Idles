package com.example.wanqing.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by dahuahua on 2017/4/30.
 */

public class IdleNameBean extends BmobObject {
    private String idle_name;

    public void setIdle_name(String s) {
        this.idle_name = s;
    }

    public String getIdle_name() {
        return this.idle_name;
    }
}
