package com.example.wanqing.model;

import android.util.Log;

import com.example.wanqing.bean.DealBean;
import com.example.wanqing.bean.IdlesInfoBean;
import com.example.wanqing.bean.UserBean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dahuahua on 2017/5/11.
 */

public class DealModel {
    public static void AddNewDeal(IdlesInfoBean idle, final OnResultBack onResultBack) {
        DealBean deal = new DealBean();

        if (BmobUser.getCurrentUser() != null) {
            deal.setUser(BmobUser.getCurrentUser(UserBean.class).getObjectId());
            deal.setBusiness(idle.getUser_id());
            deal.setPicture(idle.getPictures().get(0));
            deal.setContent(idle.getContent());
            deal.setIdle(idle);
            deal.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        onResultBack.onResultBack();
                    }
                }
            });
        } else {
            return;
        }
    }

    /*
    *   查询我的购买
    * */
    public static void QueryForUserDeal(final OnBack<DealBean> onBack) {
        BmobQuery<DealBean> query = new BmobQuery<>();

        if (BmobUser.getCurrentUser() != null) {
            query.addWhereEqualTo("user", BmobUser.getCurrentUser(UserBean.class).getObjectId());   //搜索该用户的购买信息
            query.order("-updatedAt");
            query.findObjects(new FindListener<DealBean>() {
                @Override
                public void done(List<DealBean> list, BmobException e) {
                    if (e == null) {
                        Log.d("QueryForUserDeal", "/ " + list.size());
                        onBack.onBack(list);
                    } else {
                        Log.d("QueryForUserDeal", "/ " + e.getMessage());
                        onBack.onBack(null);
                    }
                }
            });
        }
    }
}
