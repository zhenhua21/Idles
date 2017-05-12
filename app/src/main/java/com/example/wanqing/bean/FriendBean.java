package com.example.wanqing.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by dahuahua on 2017/4/29.
 */

public class FriendBean extends BmobObject {
    private UserBean user;
    private UserBean friend;

    public void setUser(UserBean user) {
        this.user = user;
    }

    public UserBean getUser() {
        return this.user;
    }

    public void setFriend(UserBean friend) {
        this.friend = friend;
    }

    public UserBean getFriend() {
        return this.friend;
    }
}
