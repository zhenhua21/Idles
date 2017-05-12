package com.example.wanqing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wanqing.idles.R;
import com.example.wanqing.viewholder.ReceiveTextViewHolder;
import com.example.wanqing.viewholder.SendTextViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.BmobUser;

import static com.example.wanqing.IdleApplication.ITEM_SEND_TEXT;
import static com.example.wanqing.IdleApplication.ITEM_RECEIVE_TEXT;

/**
 * Created by dahuahua on 2017/4/28.
 */

public class ChatAdapter extends RecyclerView.Adapter {
    private List<BmobIMMessage> msgs = new ArrayList<BmobIMMessage>();

    private String currentUid="";
    BmobIMConversation Conversation;

    public ChatAdapter(BmobIMConversation c) {
        currentUid = BmobUser.getCurrentUser().getObjectId();
        this.Conversation = c;
    }

    public int findPosition(BmobIMMessage message) {
        int index = this.getCount();
        int position = -1;
        while(index-- > 0) {
            if(message.equals(this.getItem(index))) {
                position = index;
                break;
            }
        }
        return position;
    }

    public int getCount() {
        return this.msgs == null? 0 : this.msgs.size();
    }

    public void addMessages(List<BmobIMMessage> messages) {
        msgs.addAll(0, messages);
        notifyDataSetChanged();
    }

    public void addMessage(BmobIMMessage message) {
        msgs.addAll(Arrays.asList(message));
        notifyDataSetChanged();
    }

    /**获取消息
     * @param position
     * @return
     */
    public BmobIMMessage getItem(int position){
        return this.msgs == null ? null : (position >= this.msgs.size() ? null : this.msgs.get(position));
    }

    /**移除消息
     * @param position
     */
    public void remove(int position){
        msgs.remove(position);
        notifyDataSetChanged();
    }

    public BmobIMMessage getFirstMessage() {
        if (null != msgs && msgs.size() > 0) {
            return msgs.get(0);
        } else {
            return null;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == ITEM_SEND_TEXT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_send_text_item, parent,false);
            return new SendTextViewHolder(parent.getContext(), itemView, Conversation);
        }
        if (viewType == ITEM_RECEIVE_TEXT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_receive_text_item, parent,false);
            return new ReceiveTextViewHolder(parent.getContext(), itemView, Conversation);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);

        if (type == ITEM_SEND_TEXT)
            ((SendTextViewHolder)holder).bindData(msgs.get(position));

        if (type == ITEM_RECEIVE_TEXT)
            ((ReceiveTextViewHolder)holder).bindData(msgs.get(position));

    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    @Override
    public int getItemViewType(int position) {
        BmobIMMessage message = msgs.get(position);

        return message.getFromId().equals(currentUid) ? ITEM_SEND_TEXT : ITEM_RECEIVE_TEXT;
    }
}
