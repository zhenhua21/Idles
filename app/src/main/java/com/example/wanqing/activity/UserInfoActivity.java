package com.example.wanqing.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wanqing.bean.UserBean;
import com.example.wanqing.fragments.PlusFragment;
import com.example.wanqing.fragments.UserFragment;
import com.example.wanqing.idles.R;
import com.example.wanqing.utils.UseCamera;

import java.io.File;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.example.wanqing.IdleApplication.LOGIN;
import static com.example.wanqing.IdleApplication.LOGOUT;
import static com.example.wanqing.IdleApplication.REQUEST_CAMERA;
import static com.example.wanqing.IdleApplication.UPDATE_USER_INFO;

/**
 * Created by dahuahua on 2017/4/23.
 */

public class UserInfoActivity extends Activity implements View.OnClickListener{
    ImageView img;
    TextView name, sex, age, phone, introduce;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Initialize();

    }

    private void Initialize() {
        img = (ImageView) findViewById(R.id.user_info_img);
        name = (TextView) findViewById(R.id.user_info_name);
        sex = (TextView) findViewById(R.id.user_info_sex);
        age = (TextView) findViewById(R.id.user_info_age);
        phone = (TextView) findViewById(R.id.user_info_phone);
        introduce = (TextView) findViewById(R.id.user_info_introduce);

        LinearLayout ly_img = (LinearLayout) findViewById(R.id.user_info_ly_img);
        LinearLayout ly_introduce = (LinearLayout) findViewById(R.id.user_info_ly_introduce);
        Button logout = (Button) findViewById(R.id.user_info_logout);

        ly_introduce.setOnClickListener(this);
        logout.setOnClickListener(this);
        ly_img.setOnClickListener(this);

        setUserInfo();

    }

    //同步信息
    private void setUserInfo() {
        UserBean user = BmobUser.getCurrentUser(UserBean.class);

        if (user != null) {

            if (user.getNick() != null) {
                Glide.with(getBaseContext())
                        .load(user.getNick().toString())
                        .into(img);
            }

            name.setText(user.getUsername().toString());
            sex.setText(user.getSex().toString());
            age.setText(user.getAge().toString());
            phone.setText(user.getMobilePhoneNumber().toString());

            if (user.getIntroduce() != null) {
                introduce.setText(user.getIntroduce().toString());
            }
        }
    }

    private void usingHandler(int what) {
        UserFragment.UserInfoHandler handler = new UserFragment.UserInfoHandler();
        Message msg = handler.obtainMessage();
        msg.what = what;
        handler.sendMessage(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_logout:
                BmobUser.getCurrentUser().logOut();
                //更新上一层activity
                usingHandler(UPDATE_USER_INFO);
                //更新PlusFragment
                PlusFragment.plusFragmentHandler handler = new PlusFragment.plusFragmentHandler();
                Message msg = handler.obtainMessage();
                msg.what = LOGOUT;
                handler.sendMessage(msg);

                BmobIM.getInstance().disConnect();

                finish();
                break;
            case R.id.user_info_ly_introduce:
                final EditText editText = new EditText(this);
                editText.setText("");

                new AlertDialog
                        .Builder(this)
                        .setTitle("请介绍自己")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new updateIntroduce(editText.getText().toString())).start();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.user_info_ly_img:
                startActivityForResult(UseCamera.dispatchTakePictureIntent(getBaseContext()), REQUEST_CAMERA);
                break;
            default:
                break;
        }

        usingHandler(UPDATE_USER_INFO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            new Thread(new updateUserImg()).start();
        }
    }

    private class updateIntroduce implements Runnable {
        UserBean user = new UserBean();

        public updateIntroduce(String str) {
            user.setIntroduce(str);
        }

        @Override
        public void run() {
            user.update(BmobUser.getCurrentUser(UserBean.class).getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(getBaseContext(), "用户表更新成功", Toast.LENGTH_SHORT).show();
                        BmobUser.getCurrentUser(UserBean.class).setIntroduce(user.getIntroduce());
                        //更新本activity
                        setUserInfo();
                        //更新上一层activity
                        usingHandler(UPDATE_USER_INFO);
                    }else {
                        Toast.makeText(getBaseContext(), "用户表更新失败", Toast.LENGTH_SHORT).show();
                        Log.d("updateIntroduce", "错误信息: " + e.getErrorCode());
                    }
                }
            });
        }
    }

    private class updateUserImg implements Runnable {

        @Override
        public void run() {
            final BmobFile bmobFile = new BmobFile(new File(UseCamera.mCurrentPhotoPath));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {

                    if (e == null) {    //上传成功
                        Toast.makeText(getBaseContext(), "头像上传成功", Toast.LENGTH_SHORT).show();

                        UserBean user = new UserBean();
                        user.setNick(bmobFile.getFileUrl());
                        user.update(BmobUser.getCurrentUser().getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getBaseContext(), "用户表更新成功", Toast.LENGTH_SHORT).show();
                                    BmobUser.getCurrentUser(UserBean.class).setNick(bmobFile.getFileUrl());

                                    //更新本activity
                                    setUserInfo();
                                    //更新上一层activity
                                    usingHandler(UPDATE_USER_INFO);
                                }else {
                                    Toast.makeText(getBaseContext(), "用户表更新失败", Toast.LENGTH_SHORT).show();
                                    Log.d("updateUserImg", "错误信息: " + e.getErrorCode());
                                }
                            }
                        });
                    }else {
                        Toast.makeText(getBaseContext(), "头像上传失败", Toast.LENGTH_SHORT).show();
                        Log.d("uploadblock", e.getMessage());
                    }
                }
            });
        }
    }
}
