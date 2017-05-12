package com.example.wanqing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.wanqing.bean.UserBean;
import com.example.wanqing.idles.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dahuahua on 2017/4/23.
 */

public class RegisterActivity extends Activity {
    EditText username, age, phone, password, rpassword, email;
    RadioGroup radioGroup;
    String sex;
    Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        Initialize();
    }

    private void Initialize() {
        username = (EditText) findViewById(R.id.r_name);
        radioGroup = (RadioGroup) findViewById(R.id.r_sex);
        age = (EditText) findViewById(R.id.r_age);
        phone = (EditText) findViewById(R.id.r_phone);
        email = (EditText) findViewById(R.id.r_email);
        password = (EditText) findViewById(R.id.r_password);
        rpassword = (EditText) findViewById(R.id.r_rpassword);
        register = (Button) findViewById(R.id.r_register);

        rpassword.setOnFocusChangeListener(new EditViewFocusChangeListener());
        radioGroup.setOnCheckedChangeListener(new RadioGroupCheckedListener());
    }

    private class RadioGroupCheckedListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            if (checkedId == R.id.r_male)
                sex = "男";
            else
                sex = "女";
        }
    }

    private class EditViewFocusChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            /*
            *   当失去焦点的时候
            * */
            if (!hasFocus) {
                String p = password.getText().toString();
                String rp = rpassword.getText().toString();

                if (p.equals(rp)) {
                    register.setOnClickListener(new OnClickRegisterListener());
                }else {
                    password.setText("");
                    rpassword.setText("");
                }
            }
        }
    }

    private class OnClickRegisterListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            UserBean user = new UserBean();
            user.setUsername(username.getText().toString());
            user.setSex(sex);
            user.setAge(Integer.parseInt(age.getText().toString()));
            user.setMobilePhoneNumber(phone.getText().toString());
            user.setEmail(email.getText().toString());
            user.setPassword(password.getText().toString());

            user.signUp(new SaveListener<UserBean>() {
                @Override
                public void done(UserBean userBean, BmobException e) {
                    if (e == null) {
                        Toast.makeText(getBaseContext(), "注册成功!", Toast.LENGTH_SHORT).show();
                        BmobUser.getCurrentUser().logOut();  //signup方法会自动缓存注册用户
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("OnClickRegisterListener", "/" + e.getMessage() + " /" + e.getErrorCode());
                    }
                }
            });
        }
    }
}
