package com.example.wanqing.model;

import android.content.Context;
import android.widget.Toast;

import com.example.wanqing.IdleApplication;
import com.example.wanqing.bean.IdleNameBean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by dahuahua on 2017/4/30.
 */

public class IdleNameModel {
    /*
    *   查询IdleName到本地
    * */
    public static void QueryIdleName(final OnQueryDone onQueryDone) {
        BmobQuery<IdleNameBean> query = new BmobQuery<>();
        query.order("-updatedAt");
        CheckCachePolicy(query, IdleNameBean.class);
        query.findObjects(new FindListener<IdleNameBean>() {
            @Override
            public void done(List<IdleNameBean> list, BmobException e) {
                if (e == null) {
                    onQueryDone.QueryDone(list);
                }
            }
        });
    }

    public interface OnQueryDone {
        void QueryDone(List<IdleNameBean> list);
    }

    /*
    *   上传IdleName
    * */
    public static void UpLoadIdleName(String name) {
        IdleNameBean idle = new IdleNameBean();
        idle.setIdle_name(name);
        idle.save(new SaveListener<String>() {
            @Override
            public void done(String id, BmobException e) {
                if (e != null) {
                    Toast.makeText(getContext() , "商品名上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static void CheckCachePolicy(BmobQuery query, Class clazz) {
        if (query.hasCachedResult(clazz))
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        else
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);

    }

    /*
    *   获取全局上下文
    * */
    public static Context getContext(){
        return IdleApplication.INSTANCE();
    }

}
