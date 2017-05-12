package com.example.wanqing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wanqing.bean.CommentBean;
import com.example.wanqing.idles.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dahuahua on 2017/5/10.
 */

public class CommentAdapter extends RecyclerView.Adapter {
    private ArrayList<CommentBean> list = new ArrayList<>();
    private Context context;

    public void addData(CommentBean commentBean) {
        list.add(0, commentBean);
        notifyDataSetChanged();
    }

    public void addData(List<CommentBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_comment_item, parent, false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CommentViewHolder)holder).user_name.setText(list.get(position).getUser_name());
        ((CommentViewHolder)holder).comment.setText(list.get(position).getComment());
        ((CommentViewHolder)holder).date.setText(list.get(position).getDate().getDate());
        Glide.with(context).load(list.get(position).getUser_img()).into(((CommentViewHolder)holder).user_img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView user_img;
        TextView user_name, comment, date;

        public CommentViewHolder(View itemView) {
            super(itemView);

            user_img = (ImageView) itemView.findViewById(R.id.comment_user_img);
            user_name = (TextView) itemView.findViewById(R.id.comment_user_name);
            comment = (TextView) itemView.findViewById(R.id.comment_comment);
            date = (TextView) itemView.findViewById(R.id.comment_date);
        }
    }
}
