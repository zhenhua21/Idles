package com.example.wanqing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wanqing.bean.UserBean;
import com.example.wanqing.fragments.PlusFragment;
import com.example.wanqing.fragments.UserFragment;
import com.example.wanqing.idles.R;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.example.wanqing.IdleApplication.LOGIN;
import static com.example.wanqing.IdleApplication.LOGOUT;
import static com.example.wanqing.IdleApplication.UPDATE_USER_INFO;

/**
 * Created by dahuahua on 2017/4/23.
 */

public class LoginOrRegisterActivity extends Activity implements View.OnClickListener{
    private Button register, login;
    private EditText phone_or_name, password;

    public static final int RESULT_CODE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_or_register);

        Initialize();
    }

    private void Initialize() {
        register = (Button) findViewById(R.id.lor_register);
        login = (Button) findViewById(R.id.lor_login);
        phone_or_name = (EditText) findViewById(R.id.lor_phone_or_name);
        password = (EditText) findViewById(R.id.lor_password);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    private boolean isPhone() {
        String str = phone_or_name.getText().toString();

        /*
        *   判断输入值是否为手机号
        *   ******如果恰好是11位数字的用户名*******
        * */

        if (str.length() != 11) //如果不是11位
            return false;
        else {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(0) != 1 || !Character.isDigit(str.charAt(i)))
                    return false;
            }

            return true;
        }

    }

    /*
    *   登录监听
    * */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.lor_login) {
            UserBean user = new UserBean();

            if (isPhone()) {
                user.setMobilePhoneNumber(phone_or_name.getText().toString());
            }else {
                user.setUsername(phone_or_name.getText().toString());
            }
            user.setPassword(password.getText().toString());
            user.login(new SaveListener<UserBean>() {

                @Override
                public void done(UserBean userBean, BmobException e) {
                    if (userBean == null)
                        Toast.makeText(getBaseContext(), "登录失败!", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getBaseContext(), "登录成功!", Toast.LENGTH_SHORT).show();

                        if (BmobUser.getCurrentUser(UserBean.class) != null) {

                            UserFragment.UserInfoHandler handler = new UserFragment.UserInfoHandler();
                            Message msg = handler.obtainMessage();
                            msg.what = UPDATE_USER_INFO;
                            handler.sendMessage(msg);

                            PlusFragment.plusFragmentHandler handler1 = new PlusFragment.plusFragmentHandler();
                            Message msg1 = handler1.obtainMessage();
                            msg1.what = LOGIN;
                            handler1.sendMessage(msg1);
                            Log.d("LOGIN:", "plusFragmentHandler");

                            //连接到服务器
                            UserBean user = BmobUser.getCurrentUser(UserBean.class);
                            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {

                                    }else {
                                        Log.d("BmobIM.connect", e.getMessage());
                                    }
                                }
                            });

                            finish();   //finish后代表本地缓存成功
                        }
                    }
                }
            });
        }
        if (v.getId() == R.id.lor_register) {
            startActivity(new Intent("android.intent.action.RegisterActivity"));
        }



    }
}
