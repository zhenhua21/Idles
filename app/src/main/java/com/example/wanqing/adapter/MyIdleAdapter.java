package com.example.wanqing.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wanqing.bean.IdlesInfoBean;
import com.example.wanqing.idles.R;
import com.example.wanqing.model.DownLoadData;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dahuahua on 2017/4/24.
 */

public class MyIdleAdapter extends ArrayAdapter<IdlesInfoBean> {
    private Context context;
    private int res;
    private ArrayList<IdlesInfoBean> mList = new ArrayList<IdlesInfoBean>();

    public MyIdleAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);

        this.context = context;
        this.res = resource;

        updateData();

    }

    public void updateData() {
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }

        if (BmobUser.getCurrentUser() != null) {

            new Thread(new DownLoadData("user_id", BmobUser.getCurrentUser().getObjectId()
                    , new DownLoadData.onDownLoad()
            {
                @Override
                public void success(List<IdlesInfoBean> list) {
                    mList.addAll(list);
                    notifyDataSetChanged();     //获取数据有延迟，所以必须在获取数据后调用，而且必须在success()方法内调用
                    Log.d("success", "mList.addAll(list): " + mList.size());
                }
            })).start();
        }
         //不能删
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout mLinearLayout;

        if(convertView == null){
            mLinearLayout = new LinearLayout(context);
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(res, mLinearLayout , true);
        }else {
            mLinearLayout = (LinearLayout)convertView;
        }

        if (mList != null) {
            TextView name = (TextView) mLinearLayout.findViewById(R.id.my_idle_name);
            final TextView content = (TextView) mLinearLayout.findViewById(R.id.my_idle_content);
            TextView delete = (TextView) mLinearLayout.findViewById(R.id.my_idle_delete);
            TextView isSold = (TextView) mLinearLayout.findViewById(R.id.my_idle_is_sold);
            ImageView img = (ImageView) mLinearLayout.findViewById(R.id.my_idle_img);

            name.setText(mList.get(position).getIdle_name().toString());
            content.setText(mList.get(position).getContent().toString());

            if (mList.get(position).getIsSold() == 0) {
                isSold.setText("未售出");
            }

            if (mList.get(position).getIsSold() == 1) {
                isSold.setText("已售出");
            }

            if (mList.get(position).getPictures().size() > 0) {
                Glide.with(context)
                        .load(mList.get(position).getPictures().get(0))
                        .into(img);
            }

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IdlesInfoBean idlesInfoBean = new IdlesInfoBean();
                    idlesInfoBean.setObjectId(mList.get(position).getObjectId());
                    idlesInfoBean.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                mList.remove(position);
                                notifyDataSetChanged();
                            }
                        }
                    });

                }
            });

            mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.PlusIdlesActivity");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("idle_details", mList.get(position));
                    intent.putExtras(bundle);
                    getContext().startActivity(intent);
                }
            });

        }

        return mLinearLayout;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            Log.d("success", "getCount: " + mList.size());
            return mList.size();
        } else {
            return 0;
        }

    }

}
