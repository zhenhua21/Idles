package com.example.wanqing.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by dahuahua on 2017/5/11.
 */

public class AddressBean extends BmobObject {
    private UserBean user;
    private String address;
    private String name;
    private String phone;

    public void setUser(UserBean user) {
        this.user = user;
    }

    public UserBean getUser() {
        return this.user;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

}
