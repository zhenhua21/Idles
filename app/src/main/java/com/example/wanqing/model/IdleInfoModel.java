package com.example.wanqing.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.wanqing.IdleApplication;
import com.example.wanqing.bean.IdlesInfoBean;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by dahuahua on 2017/5/10.
 */

public class IdleInfoModel {
    public static void AddLikes(IdlesInfoBean idle, final OnResultBack resultBack) {
        int likes = idle.getLikes();
        String object_id = idle.getObjectId();

        IdlesInfoBean idlesInfoBean = new IdlesInfoBean();
        idlesInfoBean.setLikes(++likes);
        Log.d("details_like", "AddLikes");
        idlesInfoBean.update(object_id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    resultBack.onResultBack();
                } else {
                    Log.d("details_like", "AddLikes/ " + e.getMessage() + "/ " + e.getErrorCode());
                }
            }
        });
    }

    public static void MakeThisOneDeal(IdlesInfoBean idle, final OnResultBack onResultBack) {
        IdlesInfoBean idlesInfoBean = new IdlesInfoBean();
        idlesInfoBean.setIsSold(1);
        idlesInfoBean.update(idle.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    onResultBack.onResultBack();
                }
            }
        });
    }

}
