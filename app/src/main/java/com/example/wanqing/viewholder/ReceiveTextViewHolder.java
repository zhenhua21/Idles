package com.example.wanqing.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cpiz.android.bubbleview.BubbleTextView;
import com.example.wanqing.idles.R;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by dahuahua on 2017/4/28.
 */

public class ReceiveTextViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
    private ImageView nick;
    private BubbleTextView text;
    private View itemView;

    private Context context;
    private BmobIMConversation conversation;

    public ReceiveTextViewHolder(Context context, View itemView, BmobIMConversation conversation) {
        super(itemView);

        this.context = context;
        this.conversation = conversation;
        this.itemView = itemView;

        InitView();
    }

    private void InitView() {
        nick = (ImageView) this.itemView.findViewById(R.id.receive_img);
        text = (BubbleTextView) this.itemView.findViewById(R.id.receive_text);

    }


    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage)o;
        final BmobIMUserInfo info = message.getBmobIMUserInfo();

        if (nick != null && info != null)
            Glide.with(context)
                .load(info.getAvatar())
                .into(nick);
        text.setText(message.getContent());

        text.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.receive_text) {
            Toast.makeText(context, "长按消息可操作消息", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
}
