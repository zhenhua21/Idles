package com.example.wanqing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wanqing.idles.R;

/**
 * Created by dahuahua on 2017/3/9.
 */

public class PlusFragment extends Fragment {
    private String title;
    private TextView plus_tv_title;

    /*
    * 创建PlusFragment的单例
    * newInstance方法中可以传入需要的参数
    * */
    public static PlusFragment newInstance(String string) {
        PlusFragment fragment = new PlusFragment();

        Bundle args = new Bundle();
        args.putString("title", string);

        fragment.setArguments(args);

        return fragment;
    }

    /*
    * 布局或者参数的实例化
    * */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getArguments().getString("title");

    }

    /*
    * 创建view的时候设置一些值
    * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plus, container, false);

        plus_tv_title = (TextView) view.findViewById(R.id.plus_tv_title);
        plus_tv_title.setText(title);

        return view;
    }
}
