package com.example.wanqing.viewholder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wanqing.bean.FriendBean;
import com.example.wanqing.bean.UserBean;
import com.example.wanqing.idles.R;
import com.example.wanqing.utils.LoadImageView;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by dahuahua on 2017/4/29.
 */

public class FriendViewHolder extends RecyclerView.ViewHolder {
    private ImageView img;
    private TextView username, last_message;

    private Context context;
    private View itemview;

    public FriendViewHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;
        this.itemview = itemView;

        InitView(itemView);
    }

    private void InitView(View v) {
        img = (ImageView) v.findViewById(R.id.conversation_item_img);
        username = (TextView) v.findViewById(R.id.conversation_item_username);
        last_message = (TextView) v.findViewById(R.id.conversation_item_user_message);
    }

    //用这个方法绑定数据
    public void bindData(FriendBean f) {
        UserBean friend = f.getFriend();

        BmobQuery<UserBean> q = new BmobQuery<UserBean>();
        q.addWhereEqualTo("objectId", friend.getObjectId());
        q.getObject(friend.getObjectId(), new QueryListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                if (e == null) {
                    /*
                    *   1、只要有人建立会话，他就会将我的信息传入
                    *   2、这个时候我是接收者
                    * */
                    LoadImageView.LoadImageView(img, userBean.getNick() == null ? null : userBean.getNick(), R.mipmap.boss);
                    username.setText(userBean.getUsername());
                    last_message.setText("");

                    itemview.setOnClickListener(new StartActivityWithConversation(userBean));
                }
            }
        });


    }

    private class StartActivityWithConversation implements View.OnClickListener {
        UserBean friend;

        public StartActivityWithConversation(UserBean friend) {
            this.friend = friend;
        }
        @Override
        public void onClick(View v) {

            Log.d("fdsfdsds", "itemview");

            //创建私聊会话，默认会保存该会话到自己的本地会话表中
            BmobIMUserInfo userInfo = new BmobIMUserInfo(friend.getObjectId(), friend.getUsername(), friend.getNick());
            BmobIM.getInstance().startPrivateConversation(userInfo, new ConversationListener() {
                @Override
                public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                    if (e == null) {
                        //在此跳转到聊天页面
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Conversation", bmobIMConversation);
                        Intent intent = new Intent("android.intent.action.ChatActivity");
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }else {
                        Log.d("PrivateConversation", e.getErrorCode() + " / " + e.getMessage());
                    }
                }
            });
        }
    }

}
