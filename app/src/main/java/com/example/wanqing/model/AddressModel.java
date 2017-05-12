package com.example.wanqing.model;

import com.example.wanqing.bean.AddressBean;
import com.example.wanqing.bean.UserBean;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dahuahua on 2017/5/11.
 */

public class AddressModel {
    /*
    *   查询我的收货地址
    * */
    public static void QueryForAddress(final OnBack onBack) {
        BmobQuery<AddressBean> query = new BmobQuery<>();

        if (UserBean.getCurrentUser() != null) {
            query.addWhereEqualTo("user", BmobUser.getCurrentUser(UserBean.class));
        } else {
            return;
        }

        query.findObjects(new FindListener<AddressBean>() {
            @Override
            public void done(List<AddressBean> list, BmobException e) {
                if (e == null)
                    onBack.onBack(list);
                else
                    onBack.onBack(null);
            }
        });
    }

    /*
    *   新增一条收货地址
    * */
    public static void AddNewAddress(String address, final OnBack<AddressBean> onBack) {
        final AddressBean addressBean = new AddressBean();

        if (UserBean.getCurrentUser() != null) {
            addressBean.setUser(BmobUser.getCurrentUser(UserBean.class));
            addressBean.setAddress(address);
            addressBean.setName(BmobUser.getCurrentUser(UserBean.class).getUsername());
            addressBean.setPhone(BmobUser.getCurrentUser(UserBean.class).getMobilePhoneNumber());
            addressBean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        onBack.onBack(Arrays.asList(addressBean));
                    }
                }
            });
        } else {
            return;
        }
    }

    /*
    *   删除一条地址
    * */
    public static void DeleteAddress(String id, final OnResultBack onResultBack) {
        AddressBean addressBean = new AddressBean();
        addressBean.delete(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    onResultBack.onResultBack();
                }
            }
        });
    }
}
