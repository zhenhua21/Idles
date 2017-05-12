package com.example.wanqing.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wanqing.bean.IdlesInfoBean;
import com.example.wanqing.bean.UserBean;
import com.example.wanqing.fragments.BrowseListFragment;
import com.example.wanqing.fragments.PlusFragment;
import com.example.wanqing.idles.R;
import com.example.wanqing.model.IdleNameModel;
import com.example.wanqing.utils.UseCamera;
import com.google.android.flexbox.FlexboxLayout;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import static com.example.wanqing.IdleApplication.LOGIN;
import static com.example.wanqing.IdleApplication.UPDATE_IDLE_LIST;
import static com.example.wanqing.utils.UseCamera.mCurrentPhotoPath;

/**
 * Created by dahuahua on 2017/3/30.
 */

public class PlusIdlesActivity extends Activity {
    private Spinner classify;
    private EditText idle_name, content, price, phone;
    private ImageView plus;
    private Button submit;
    private FlexboxLayout mFlexboxLayout;

    private String[] myClassify = {"点击选择", "电子", "书籍", "运动", "乐器", "衣物", "鞋靴", "箱包", "其他"};

    private ArrayList<String> phonePaths = new ArrayList<String>();

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CAMERA = 3;
    private static final int PICTURE_RESULT = 2;

    ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_idles);

        Initialize();
        //CreateFromMyIdle();
    }

    private void CreateFromMyIdle() {
        if (getIntent().getExtras() != null) {
            IdlesInfoBean mIdle = (IdlesInfoBean) getIntent().getExtras().getSerializable("idle_details");

            if (mIdle != null) {
                classify.setSelection(0);
                idle_name.setText(mIdle.getIdle_name());
                content.setText(mIdle.getContent());
                price.setText(mIdle.getPrice() + "");
                phone.setText(mIdle.getPhone());
                mFlexboxLayout.removeAllViews();
                for (int i = 0; i < mIdle.getPictures().size(); i++) {
                    ImageView imageView = getImageView();
                    Glide.with(getBaseContext())
                            .load(mIdle.getPictures().get(i))
                            .into(imageView);
                    mFlexboxLayout.addView(imageView, i);
                    Log.d("dsadasdf", "/ " + mIdle.getPictures().get(i));

                }
                mFlexboxLayout.addView(plus, mIdle.getPictures().size());
            }
        }
    }

    private void Initialize() {
        classify = (Spinner) findViewById(R.id.plus_sp_class);
        idle_name = (EditText) findViewById(R.id.plus_et_idle_name);
        content = (EditText) findViewById(R.id.plus_et_content);
        price = (EditText) findViewById(R.id.plus_et_price);
        phone = (EditText)findViewById(R.id.plus_et_phone);
        plus = (ImageView) findViewById(R.id.plus_ib_plus);
        submit = (Button) findViewById(R.id.plus_bt_submit);
        mFlexboxLayout = (FlexboxLayout) findViewById(R.id.plus_Flexbox);

        pd = new ProgressDialog(this);
        pd.setTitle("进度");
        pd.setMax(100);

        plus.setOnClickListener(new onClickPlusIconListener());
        submit.setOnClickListener(new onClickTakeInListener());

        checkPermission();

    }

    public void plusPictureDialog() {
        final String items[] = {"从相册获取", "使用相机获取"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(UseCamera.dispatchTakePictureIntent(getBaseContext()), REQUEST_TAKE_PHOTO);
            }
        });
        builder.create().show();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
        }
    }

    /*
        *   调用相机的回调
        * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            //Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show();

            ImageView imageView = getImageView();

            Glide.with(this)
                    .load(UseCamera.mCurrentPhotoPath)
                    .into(imageView);

            ArrayList<ImageView> mImageViews = new ArrayList<ImageView>();

            for (int i = 0; i < mFlexboxLayout.getChildCount(); i++)
                mImageViews.add((ImageView) mFlexboxLayout.getChildAt(i));

            mFlexboxLayout.removeAllViews();
            mFlexboxLayout.addView(imageView, 0);
            for (int i = 0; i < mImageViews.size(); i++) {
                mFlexboxLayout.addView(mImageViews.get(i), -1);
            }
        }

        phonePaths.add(UseCamera.mCurrentPhotoPath);
    }

    /*
        *   得到一个ImageView
        * */
    public ImageView getImageView() {
        ImageView mImageView = new ImageView(this);
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(200, 200);
        lp.rightMargin = 12;
        mImageView.setLayoutParams(lp);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        return mImageView;
    }

    /*
    *   点击增加图片按钮的出发事件
    * */
    private class onClickPlusIconListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            plusPictureDialog();

        }
    }

    /*
    *   点击提交按钮的触发事件
    * */
    private class onClickTakeInListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            pd.show();
            new upLoadIdlesInfo().run();

        }
    }

    /*
    *   往服务器上传数据属于耗时操作，需开启一个子线程
    * */
    private class upLoadIdlesInfo implements Runnable {
        String[] paths = new String[phonePaths.size()];

        @Override
        public void run() {
            pd.setTitle("商品信息上传中");

            phonePaths.toArray(paths);

            BmobFile.uploadBatch(paths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if (list1.size() == phonePaths.size()) {
                        /*
                        *   获取各个组件的值，set到bean中
                        * */
                        final IdlesInfoBean bean = new IdlesInfoBean();

                        bean.setClassify(myClassify[classify.getSelectedItemPosition()]);
                        bean.setIdle_name(idle_name.getText().toString());
                        bean.setContent(content.getText().toString());
                        bean.setPhone(phone.getText().toString());
                        bean.setPrice(Integer.parseInt(price.getText().toString()));
                        bean.setPictures(list1);
                        bean.setLikes(0);
                        bean.setIsSold(0);
                        bean.setUser_id(BmobUser.getCurrentUser(UserBean.class).getObjectId());
                        bean.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getBaseContext(), "数据库插入成功", Toast.LENGTH_SHORT).show();

                                    PlusFragment.plusFragmentHandler handler = new PlusFragment.plusFragmentHandler();
                                    Message msg = handler.obtainMessage();
                                    msg.what = LOGIN;
                                    handler.sendMessage(msg);

                                    BrowseListFragment.BrowseListHandler handler1 = new BrowseListFragment.BrowseListHandler();
                                    Message msg1 = handler1.obtainMessage();
                                    msg1.what = UPDATE_IDLE_LIST;
                                    handler1.sendMessage(msg1);

                                    IdleNameModel.UpLoadIdleName(bean.getIdle_name());

                                    finish();
                                }
                            }
                        });
                    }



                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                    pd.setProgress(i3);

                    pd.setMessage(i3 + "%(正在上次第" + i + "个文件)");
                    if (i3 == 100)
                        pd.dismiss();
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(getBaseContext(), "图片上传失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
