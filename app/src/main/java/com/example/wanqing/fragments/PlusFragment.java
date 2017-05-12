package com.example.wanqing.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanqing.adapter.MyIdleAdapter;
import com.example.wanqing.bean.IdlesInfoBean;
import com.example.wanqing.bean.UserBean;
import com.example.wanqing.idles.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.example.wanqing.IdleApplication.LOGIN;
import static com.example.wanqing.IdleApplication.LOGOUT;


/**
 * Created by dahuahua on 2017/3/9.
 */

public class PlusFragment extends Fragment {
    private ImageButton plus;
    private ListView mIdleList;
    private static TextView mTitle;

    private static MyIdleAdapter adapter;
    //private plusFragmentHandler handler;
    /*
    * 创建PlusFragment的单例
    * newInstance方法中可以传入需要的参数
    * */
    public static PlusFragment newInstance(String string) {
        PlusFragment fragment = new PlusFragment();


        return fragment;
    }

    /*
    * 创建view的时候设置一些值
    * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plus, container, false);

        Initialize(view);

        return view;
    }

    private void Initialize(View view) {
        plus = (ImageButton) view.findViewById(R.id.plus_ib_add);
        mIdleList = (ListView) view.findViewById(R.id.plus_lv_mIdles);
        mTitle = (TextView) view.findViewById(R.id.plus_tv_title);
        adapter = new MyIdleAdapter(getContext(), R.layout.layout_my_item);

        if (BmobUser.getCurrentUser() != null) {
            mTitle.setVisibility(View.INVISIBLE);
        }

        plus.setOnClickListener(new onClickPlusListener());
        mIdleList.setAdapter(adapter);


    }

    private class onClickPlusListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (BmobUser.getCurrentUser(UserBean.class) != null) {
                startActivity(new Intent("android.intent.action.PlusIdlesActivity"));
            }else {
                startActivity(new Intent("android.intent.action.LoginOrRegisterActivity"));
            }

        }
    }

    public static class plusFragmentHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN:     //用户在线
                    adapter.updateData();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            BmobQuery<IdlesInfoBean> query = new BmobQuery<IdlesInfoBean>();
                            query.addWhereEqualTo("objectid", BmobUser.getCurrentUser().getObjectId());
                            query.findObjects(new FindListener<IdlesInfoBean>() {
                                @Override
                                public void done(List<IdlesInfoBean> list, BmobException e) {
                                    if (e == null) {
                                        mTitle.setVisibility(View.INVISIBLE);
                                    }else {
                                        mTitle.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }).start();

                    break;
                case LOGOUT:     //用户离线
                    adapter.updateData();
                    mTitle.setVisibility(View.VISIBLE);
                    break;
                default:
                    mTitle.setVisibility(View.VISIBLE);
                    break;
            }

        }
    }



}
