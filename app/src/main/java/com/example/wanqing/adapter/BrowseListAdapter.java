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

import com.bumptech.glide.Glide;
import com.example.wanqing.bean.IdlesInfoBean;
import com.example.wanqing.idles.R;
import com.example.wanqing.model.DownLoadData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dahuahua on 2017/3/28.
 */

public class BrowseListAdapter extends ArrayAdapter<IdlesInfoBean> {
    private ArrayList<IdlesInfoBean> mlist = new ArrayList<IdlesInfoBean>();
    private Context context;
    private int res;

    public BrowseListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);

        this.context = context;
        this.res = resource;

        setDate();
    }

    /*
    *   增加数据源
    * */
    public void setDate() {
        mlist.clear();
        DownLoadData downLoadData = new DownLoadData("user_id", null, new DownLoadData.onDownLoad() {

            @Override
            public void success(List<IdlesInfoBean> list) {
                mlist.addAll(list);
                notifyDataSetChanged();
            }
        });
        new Thread(downLoadData).start();
    }

    /*
    *   重写getView方法，绑定数据和布局
    * */
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //getItem(position);
        Log.d("Adapter", "convertView is not null now");
        LinearLayout mLinearLayout;

        if(convertView == null){
            mLinearLayout = new LinearLayout(context);
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(res, mLinearLayout , true);
        }else {
            mLinearLayout = (LinearLayout)convertView;
        }

        if (mlist != null) {
            TextView name = (TextView) mLinearLayout.findViewById(R.id.ly_tv_name);
            TextView content = (TextView) mLinearLayout.findViewById(R.id.ly_tv_content);
            ImageView picture = (ImageView) mLinearLayout.findViewById(R.id.ly_iv_img);
            TextView likes = (TextView) mLinearLayout.findViewById(R.id.ly_tv_like);

            name.setText(mlist.get(position).getIdle_name());
            likes.setText("点赞: " + mlist.get(position).getLikes());
            content.setText(mlist.get(position).getContent());
            Glide.with(getContext())
                    .load(mlist.get(position).getPictures().get(0))
                    .into(picture);
        }

        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.IdleDetailsActivity");
                Bundle bundle = new Bundle();
                bundle.putSerializable("idle_details", mlist.get(position));
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });

        return mLinearLayout;
    }

    /*
    *    第一、没有执行getview往往是因为没有显示页面。
    *    这个时候，我们首先要保证确实有有效的数据传到了自定义的适配器里。
    *    因为如果getcount（）的返回值是0的话，getview是不会被执行的。
    *
    * */
    @Override
    public int getCount() {
        if (mlist != null) {
            return mlist.size();
        }else {
            return 0;
        }


    }
}
