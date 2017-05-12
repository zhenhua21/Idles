package com.example.wanqing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wanqing.bean.IdlesInfoBean;
import com.example.wanqing.idles.R;

import java.util.ArrayList;

/**
 * Created by dahuahua on 2017/5/10.
 */

public class ResultAdapter extends RecyclerView.Adapter {
    private OnItemClickListener onItemClickListener;
    private ArrayList<IdlesInfoBean> mList = new ArrayList<IdlesInfoBean>();
    private Context context;

    public ResultAdapter(Context context, ArrayList<IdlesInfoBean> List) {
        this.mList.addAll(List);
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ItemViewHolder)holder).idle_name.setText(mList.get(position).getIdle_name());
        ((ItemViewHolder)holder).content.setText(mList.get(position).getContent());
        ((ItemViewHolder)holder).likes.setText("点赞: " + mList.get(position).getLikes());
        Glide.with(context).load(mList.get(position).getPictures().get(0).toString()).into(((ItemViewHolder)holder).picture);

        ((ItemViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.ItemClick(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView idle_name, content, likes;
        ImageView picture;

        public ItemViewHolder(View itemView) {
            super(itemView);

            idle_name = (TextView) itemView.findViewById(R.id.ly_tv_name);
            content = (TextView) itemView.findViewById(R.id.ly_tv_content);
            likes = (TextView) itemView.findViewById(R.id.ly_tv_like);
            picture = (ImageView) itemView.findViewById(R.id.ly_iv_img);
        }
    }
}
