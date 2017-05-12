package com.example.wanqing.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wanqing.bean.UserBean;
import com.example.wanqing.idles.R;

import cn.bmob.v3.BmobUser;

import static com.example.wanqing.IdleApplication.UPDATE_USER_INFO;

/**
 * Created by dahuahua on 2017/3/9.
 */

public class UserFragment extends Fragment {
    private LinearLayout mInfo, mMessage, mDeal, mAddress, mSettings;

    private static View view = null;

    /*
    * 创建UserFragment的单例
    * */
    public static UserFragment newInstance(String string) {
        UserFragment fragment = new UserFragment();

        return fragment;

    }

    /*
    * 初始化布局
    * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);

        Initialize(view);

        return view;
    }

    private void Initialize(View view) {
        /*
        *   获取UI控件的实例
        * */
        mInfo = (LinearLayout) view.findViewById(R.id.user_information);

        mMessage = (LinearLayout) view.findViewById(R.id.user_ly_mMessage);
        mDeal = (LinearLayout) view.findViewById(R.id.user_ly_mDeal);
        mAddress = (LinearLayout) view.findViewById(R.id.user_ly_mAddress);
        mSettings = (LinearLayout) view.findViewById(R.id.user_ly_settings);

        /*
        *   设置监听
        * */
        mMessage.setOnClickListener(new myOnClickListener());
        mDeal.setOnClickListener(new myOnClickListener());
        mAddress.setOnClickListener(new myOnClickListener());
        mSettings.setOnClickListener(new myOnClickListener());
        mInfo.setOnClickListener(new myOnClickListener());

        UserInfo();

    }

    /*
    *   判断用户是否已经登录，如果已经登录则启动用户信息activity，如果没有登录则启动登录注册activity
    * */
    private void CheckLoginOrOther(Intent intent) {
        UserBean user = BmobUser.getCurrentUser(UserBean.class);

        /*
        *   如果用户登录，则打开指定activity，否则引导用户登录
        * */
        if (user != null) { //用户登录
            startActivity(intent);
        }else {
            startActivity(new Intent("android.intent.action.LoginOrRegisterActivity"));
        }

    }

    private static void UserInfo() {
        UserBean user = BmobUser.getCurrentUser(UserBean.class);
        ImageView user_img = (ImageView) view.findViewById(R.id.user_iv_img);;
        TextView name = (TextView) view.findViewById(R.id.user_tv_name);
        TextView introduce = (TextView) view.findViewById(R.id.user_tv_introduce);

        if (user != null) {

            if (user.getNick() != null) {
                Glide.with(view.getContext())
                        .load(user.getNick())
                        .into(user_img);

            }

            name.setText(user.getUsername().toString());

            if (user.getIntroduce() != null) {
                introduce.setText(user.getIntroduce().toString());
            }else {
                introduce.setText("该用户什么都没留下...");
            }
        } else {
            user_img.setImageResource(R.mipmap.ic_launcher);
            name.setText("未登录");
            introduce.setText("请登录");
        }
    }

    private class myOnClickListener implements View.OnClickListener {
        //*************************Intent需分别带入信息************************//
        Intent otherIntent = new Intent("android.intent.action.MyDealActivity");

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.user_information:
                    CheckLoginOrOther(new Intent("android.intent.action.UserInfoActivity"));
                    break;
                case R.id.user_ly_mMessage:
                    CheckLoginOrOther(new Intent("android.intent.action.FriendActivity"));
                    break;
                case R.id.user_ly_mDeal:
                    CheckLoginOrOther(new Intent("android.intent.action.MyDealActivity"));
                    break;
                case R.id.user_ly_mAddress:
                    CheckLoginOrOther(new Intent("android.intent.action.MyAddressActivity"));
                    break;
                case R.id.user_ly_settings:
                    //CheckLoginOrOther(otherIntent);
                    break;
                default:
                    break;

            }
        }
    }

    public static class UserInfoHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_USER_INFO) {
                UserInfo();
            }
        }
    }
}
