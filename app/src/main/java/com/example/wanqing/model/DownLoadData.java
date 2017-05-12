package com.example.wanqing.model;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.wanqing.bean.IdlesInfoBean;
import com.example.wanqing.bean.UserBean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by dahuahua on 2017/4/27.
 */

public class DownLoadData implements Runnable {
    //获取数据

    private onDownLoad downLoad = null;
    private String key = null;
    private String value = null;

    /*
    *   1、本类实现了Runable接口
    *   2、key-value是查找条件，null表示没有条件
    * */
    public DownLoadData(String key, String value, onDownLoad downLoad) {
        this.downLoad = downLoad;
        this.key = key;
        this.value = value;
    }

    @Override
    public void run() {
        BmobQuery<IdlesInfoBean> query = new BmobQuery<IdlesInfoBean>();


        if (key != null && value != null) //如果两个都不等于null
            query.addWhereEqualTo(key, value);
        if (key != null && value == null) //如果key不等于null,但value 等于 null
            if (BmobUser.getCurrentUser() != null)
                query.addWhereNotEqualTo("user_id", BmobUser.getCurrentUser().getObjectId());
        query.order("-updatedAt");
        query.addWhereNotEqualTo("isSold", 1);
        //CheckCachePolicy(query, IdlesInfoBean.class);
        query.findObjects(new FindListener<IdlesInfoBean>() {
            @Override
            public void done(List<IdlesInfoBean> list, BmobException e) {

                if (e == null) {
                    downLoad.success(list);
                }
            }
        });
    }

    private void CheckCachePolicy(BmobQuery query, Class clazz) {
        if (query.hasCachedResult(clazz))
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        else
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);

    }

    public interface onDownLoad {
        void success(List<IdlesInfoBean> list);
    }
}
