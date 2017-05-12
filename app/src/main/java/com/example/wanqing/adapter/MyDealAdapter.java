package com.example.wanqing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wanqing.bean.DealBean;
import com.example.wanqing.idles.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dahuahua on 2017/5/11.
 */

public class MyDealAdapter extends RecyclerView.Adapter {
    private ArrayList<DealBean> list = new ArrayList<>();
    private Context context;

    public void addData(List<DealBean> list) {
        this.list.addAll(list);
        Log.d("QueryForUserDeal", "/ " + list.size());
        notifyDataSetChanged();
    }

    public void addData(DealBean dealBean) {
        list.add(0, dealBean);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_deal_item, parent, false);

        return new MyDealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyDealViewHolder)holder).content.setText(list.get(position).getContent());
        Glide.with(context).load(list.get(position).getPicture()).into(((MyDealViewHolder)holder).img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyDealViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView content;

        public MyDealViewHolder(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.deal_item_img);
            content = (TextView) itemView.findViewById(R.id.deal_item_content);
        }
    }
}
