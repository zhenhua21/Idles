package com.example.wanqing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wanqing.adapter.AddressAdapter;
import com.example.wanqing.adapter.OnItemClickListener;
import com.example.wanqing.bean.AddressBean;
import com.example.wanqing.idles.R;
import com.example.wanqing.model.AddressModel;
import com.example.wanqing.model.OnBack;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.wanqing.IdleApplication.RESULT_CODE;

/**
 * Created by dahuahua on 2017/5/11.
 */

public class MyAddressActivity extends Activity implements View.OnClickListener {
    private AddressAdapter adapter;
    private static ArrayList<AddressBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        InitView();
        checkOutSwitch();
    }

    private void InitView() {
        Button add_address = (Button) findViewById(R.id.my_address_add_address);
        RecyclerView address_list = (RecyclerView) findViewById(R.id.my_address_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        adapter = new AddressAdapter();

        address_list.setLayoutManager(manager);
        address_list.setAdapter(adapter);
        address_list.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));
        add_address.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                AddressModel.QueryForAddress(new OnBack() {
                    @Override
                    public void onBack(List list) {
                        if (list != null) {
                            mList.addAll(list);
                            adapter.addData(list);
                        }
                    }
                });
            }
        }).start();

    }

    private void checkOutSwitch() {
        int i = getIntent().getIntExtra("switch", 0);

        if (i == 1) {

            adapter.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void ItemClick(View view, int position) {
                    DealActivity.DealHandler handler = new DealActivity.DealHandler();
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    msg.obj = mList.get(position);
                    handler.sendMessage(msg);
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        final EditText et = new EditText(this);

        et.setBackgroundResource(R.drawable.input_rectangle);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入新的地址");
        builder.setView(et);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (et.getText().length() > 0)
                            AddressModel.AddNewAddress(et.getText().toString(), new OnBack<AddressBean>() {
                                @Override
                                public void onBack(List<AddressBean> list) {
                                    Toast.makeText(getBaseContext(), "地址添加成功", Toast.LENGTH_SHORT).show();
                                    mList.add(0, list.get(0));
                                    adapter.addData(list.get(0));
                                }
                            });
                    }
                }).start();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
}
