package com.example.wanqing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wanqing.idles.R;

/**
 * Created by dahuahua on 2017/3/9.
 */

public class UserFragment extends Fragment {
    private String title;
    private ImageView user_img;
    private TextView user_tv_title, introduce;
    private LinearLayout mMessage, mDeal, mIdle, mAddress, mSettings;

    /*
    * 创建UserFragment的单例
    * */
    public static UserFragment newInstance(String string) {
        UserFragment fragment = new UserFragment();

        Bundle args = new Bundle();
        args.putString("title", string);

        fragment.setArguments(args);

        return fragment;

    }

    /*
    * 初始化变量
    * */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getArguments().getString("title");
    }

    /*
    * 初始化布局
    * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        user_tv_title = (TextView) view.findViewById(R.id.user_tv_title);
        mMessage = (LinearLayout) view.findViewById(R.id.user_ly_mMessage);
        mIdle = (LinearLayout) view.findViewById(R.id.user_ly_mIdle);
        mDeal = (LinearLayout) view.findViewById(R.id.user_ly_mDeal);
        mAddress = (LinearLayout) view.findViewById(R.id.user_ly_mAddress);
        mSettings = (LinearLayout) view.findViewById(R.id.user_ly_settings);

        Initialize();

        return view;
    }

    private void Initialize() {
        user_tv_title.setText(title);

        mMessage.setOnClickListener(new UserSettingsOnClickListener());
        mIdle.setOnClickListener(new UserSettingsOnClickListener());
        mDeal.setOnClickListener(new UserSettingsOnClickListener());
        mAddress.setOnClickListener(new UserSettingsOnClickListener());
        mSettings.setOnClickListener(new UserSettingsOnClickListener());
    }

    public class UserSettingsOnClickListener implements View.OnClickListener {
        public UserSettingsOnClickListener() {

        }

        @Override
        public void onClick(View v) {

        }
    }
}
