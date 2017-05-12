package com.example.wanqing.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cpiz.android.bubbleview.BubbleTextView;
import com.example.wanqing.bean.UserBean;
import com.example.wanqing.idles.R;
import com.example.wanqing.utils.LoadImageView;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by dahuahua on 2017/4/28.
 */

public class SendTextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private BmobIMConversation conversation;
    private Context context;
    private View itemView;

    private ImageView fail_resend;
    private BubbleTextView text;
    private ImageView nick;

    private static BmobIMMessage message;

    public SendTextViewHolder(Context context, View itemView, BmobIMConversation conversation) {
        super(itemView);

        this.conversation = conversation;
        this.context = context;
        this.itemView = itemView;

        InitView();
    }

    private void InitView() {

        fail_resend = (ImageView) this.itemView.findViewById(R.id.send_fail_resend);
        text = (BubbleTextView) this.itemView.findViewById(R.id.send_content);
        nick = (ImageView) this.itemView.findViewById(R.id.send_img);
    }

    /*
    *   Object o ,可以理解为多态的一种
    * */
    public void bindData(Object o) {
        message = (BmobIMMessage) o;
        final BmobIMUserInfo info = message.getBmobIMUserInfo();

        /*
        *   1、发送方 -----> 我
        *   2、建立会话时，只传入了接收方的userInfo,没有传入发送方的userInfo
        *   3、建立会话后，sdk自动创建本地用户表，表中信息不会有 "我" ！
        *   4、当 接收方 发送消息后，却能够更新 我 的消息，所以不明白 我 的userInfo如何被获取和更新
        * */
        String avatar = BmobUser.getCurrentUser(UserBean.class).getNick();
        text.setText(message.getContent());
        LoadImageView.LoadImageView(nick, avatar == null ? null : avatar, R.mipmap.boss);

        int status = message.getSendStatus();
        if (status == BmobIMSendStatus.SENDFAILED.getStatus()) {
            fail_resend.setVisibility(View.VISIBLE);
        }
        if (status == BmobIMSendStatus.SENDED.getStatus()) {
            fail_resend.setVisibility(View.INVISIBLE);
        }

        text.setOnLongClickListener(this);
        fail_resend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_fail_resend) {
            conversation.resendMessage(message, new MessageSendListener() {
                @Override
                public void onStart(BmobIMMessage bmobIMMessage) {
                    Toast.makeText(context, "重新发送", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                    if (e == null) {
                        fail_resend.setVisibility(View.GONE);
                    } else {
                        fail_resend.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.send_content) {
            Toast.makeText(context, "长按消息可操作消息", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
}
