package com.example.wanqing.model;

import com.example.wanqing.bean.CommentBean;
import com.example.wanqing.bean.IdlesInfoBean;
import com.example.wanqing.bean.UserBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dahuahua on 2017/5/10.
 */

public class CommentModel {
    /*
    *   查询到商品对应的评价
    * */
    public static void QueryForIdleComment(IdlesInfoBean idle, final OnBack queryComment) {
        final BmobQuery<CommentBean> query = new BmobQuery<>();
        query.addWhereEqualTo("idle", idle);
        query.order("-updatedAt");
        query.groupby(new String[]{"user"});    //回复功能的考虑
        //CheckCachePolicy(query, CommentBean.class);
        query.findObjects(new FindListener<CommentBean>() {
            @Override
            public void done(List<CommentBean> list, BmobException e) {
                if (e == null) {
                    queryComment.onBack(list);
                } else {
                    queryComment.onBack(null);
                }
            }
        });
    }

    /*
    *   新增评论
    * */
    public static void InsertComment(String comment, IdlesInfoBean idle, final OnBack queryComment) {
        final CommentBean commentBean = new CommentBean();
        commentBean.setComment(comment);
        commentBean.setIdle(idle);
        commentBean.setType(0); //表示评论
        commentBean.setBusiness_id(idle.getUser_id());
        commentBean.setDate(new BmobDate(new Date(System.currentTimeMillis())));    //设置时间
        if (BmobUser.getCurrentUser() != null) {
            UserBean user = BmobUser.getCurrentUser(UserBean.class);
            commentBean.setUser(user);
            commentBean.setUser_img(user.getNick());
            commentBean.setUser_name(user.getUsername());
        } else {
            commentBean.setUser(null);
            commentBean.setUser_img(null);
            commentBean.setUser_name("匿名用户");
        }
        commentBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    List<CommentBean> list = new ArrayList<CommentBean>();
                    list.add(commentBean);
                    queryComment.onBack(list);
                } else {
                    queryComment.onBack(null);
                }
            }
        });

    }

    /*
    *   缓存策略
    * */
    private static void CheckCachePolicy(BmobQuery query, Class clazz) {
        if (query.hasCachedResult(clazz))
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        else
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);

    }
}
