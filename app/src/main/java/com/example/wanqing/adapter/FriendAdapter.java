package com.example.wanqing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.wanqing.bean.FriendBean;
import com.example.wanqing.bean.UserBean;
import com.example.wanqing.idles.R;
import com.example.wanqing.viewholder.FriendViewHolder;

import java.util.ArrayList;
import java.util.Arrays;

import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by dahuahua on 2017/4/29.
 */

/*
*   1、数据：friend表中查询到的数据
*   2、更新数据：
*
*
* */
public class FriendAdapter extends RecyclerView.Adapter {
    private ArrayList<FriendBean> mList = new ArrayList<FriendBean>();

//    public FriendAdapter(ArrayList<FriendBean> List) {
//        mList.clear();
//        mList.addAll(List);
//
//    }

    /*
    *   添加单个朋友
    * */
    public void addFriend(FriendBean friend) {
        mList.addAll(Arrays.asList(friend));
        notifyDataSetChanged();
    }

    /*
    *   添加多个朋友
    * */
    public void addFriends(ArrayList<FriendBean> friends) {
        mList.addAll(0, friends);
        notifyDataSetChanged();
    }

    /*
    *   获取朋友列表的Friend
    * */
    public FriendBean getItem(int postion) {
        return mList.get(postion);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout mLinearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation_item, parent, false);
        FriendViewHolder viewHolder = new FriendViewHolder(parent.getContext(), mLinearLayout);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FriendViewHolder)holder).bindData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
