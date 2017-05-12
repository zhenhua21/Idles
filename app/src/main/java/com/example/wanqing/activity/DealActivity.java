package com.example.wanqing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wanqing.bean.AddressBean;
import com.example.wanqing.bean.DealBean;
import com.example.wanqing.bean.IdlesInfoBean;
import com.example.wanqing.idles.R;
import com.example.wanqing.model.AddressModel;
import com.example.wanqing.model.DealModel;
import com.example.wanqing.model.IdleInfoModel;
import com.example.wanqing.model.OnBack;
import com.example.wanqing.model.OnResultBack;

import java.util.List;

import static com.example.wanqing.IdleApplication.REQUEST_CAMERA;
import static com.example.wanqing.IdleApplication.REQUEST_CODE;
import static com.example.wanqing.IdleApplication.RESULT_CODE;

/**
 * Created by dahuahua on 2017/5/11.
 */

public class DealActivity extends Activity implements View.OnClickListener {
    private IdlesInfoBean mIdlesInfo = new IdlesInfoBean();
    private static TextView name;
    private static TextView phone;
    private static TextView address;
    private static AddressBean addressBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        mIdlesInfo = (IdlesInfoBean) getIntent().getExtras().getSerializable("idle");

        InitView();
    }

    private void InitView() {
        ImageView idle_img = (ImageView) findViewById(R.id.deal_img);
        TextView content = (TextView) findViewById(R.id.deal_content);
        TextView price = (TextView) findViewById(R.id.deal_price);
        name = (TextView) findViewById(R.id.deal_name);
        phone = (TextView) findViewById(R.id.deal_phone);
        address = (TextView) findViewById(R.id.deal_address);

        Glide.with(getBaseContext()).load(mIdlesInfo.getPictures().get(0)).into(idle_img);
        content.setText(mIdlesInfo.getContent());
        price.setText(mIdlesInfo.getPrice() + " 元");
        new Thread(new Runnable() {
            @Override
            public void run() {
                AddressModel.QueryForAddress(new OnBack<AddressBean>() {
                    @Override
                    public void onBack(List<AddressBean> list) {
                        if (list != null) {
                            TextView address = (TextView) findViewById(R.id.deal_address);
                            address.setText(list.get(0).getAddress());
                        }
                    }
                });
            }
        }).start();

        TextView deal = (TextView) findViewById(R.id.deal_deal);
        LinearLayout switch_address = (LinearLayout) findViewById(R.id.deal_switch_address);

        deal.setOnClickListener(this);
        switch_address.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                AddressModel.QueryForAddress(new OnBack<AddressBean>() {
                    @Override
                    public void onBack(List<AddressBean> list) {
                        if (list != null) {
                            addressBean = list.get(0);
                            SwitchAddress();
                        }

                    }
                });
            }
        }).start();
    }

    /*
    *   减少重复代码
    * */
    private static void SwitchAddress() {
        name.setText(addressBean.getName());
        phone.setText(addressBean.getPhone());
        address.setText(addressBean.getAddress());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.deal_deal) {
            Toast.makeText(getBaseContext(), "正在跳转支付宝(微信)...", Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(), "正在支付...", Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(), "支付完成...", Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DealModel.AddNewDeal(mIdlesInfo, new OnResultBack() {
                        @Override
                        public void onResultBack() {
                            Toast.makeText(getBaseContext(), "生成订单...", Toast.LENGTH_SHORT).show();
                            IdleInfoModel.MakeThisOneDeal(mIdlesInfo, new OnResultBack() {
                                @Override
                                public void onResultBack() {
                                    Toast.makeText(getBaseContext(), "购买成功...", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }
                    });
                }
            }).start();

        }

        if (v.getId() == R.id.deal_switch_address) {
            Intent intent = new Intent(getBaseContext(), MyAddressActivity.class);
            intent.putExtra("switch", 1);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    public static class DealHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                addressBean = (AddressBean) msg.obj;
                Log.d("DealHandler", "/ " + addressBean.getAddress());
                SwitchAddress();
            }

        }
    }
}
