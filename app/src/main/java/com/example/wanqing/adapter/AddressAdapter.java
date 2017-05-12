package com.example.wanqing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wanqing.bean.AddressBean;
import com.example.wanqing.idles.R;
import com.example.wanqing.model.AddressModel;
import com.example.wanqing.model.OnResultBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dahuahua on 2017/5/11.
 */

public class AddressAdapter extends RecyclerView.Adapter {
    private OnItemClickListener onItemClickListener;
    private ArrayList<AddressBean> mList = new ArrayList<>();

    public void addData(List<AddressBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(AddressBean addressBean) {
        this.mList.add(0, addressBean);
        notifyDataSetChanged();
    }

    //购买商品时选择地址时调用
    public void setOnItemClickListener(OnItemClickListener Listener) {
        this.onItemClickListener = Listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_address_item, parent, false);

        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((AddressViewHolder)holder).name.setText(mList.get(position).getName());
        ((AddressViewHolder)holder).phone.setText(mList.get(position).getPhone());
        ((AddressViewHolder)holder).address.setText(mList.get(position).getAddress());
        ((AddressViewHolder)holder).delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AddressModel.DeleteAddress(mList.get(position).getObjectId(), new OnResultBack() {
                            @Override
                            public void onResultBack() {
                                mList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            }
        });
        ((AddressViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.ItemClick(null, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, address, delete;

        public AddressViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.address_item_name);
            phone = (TextView) itemView.findViewById(R.id.address_item_phone);
            address = (TextView) itemView.findViewById(R.id.address_item_address);
            delete = (TextView) itemView.findViewById(R.id.address_item_delete);
        }
    }

}
