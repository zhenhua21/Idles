package com.example.wanqing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.wanqing.adapter.MyDealAdapter;
import com.example.wanqing.adapter.MyIdleAdapter;
import com.example.wanqing.bean.DealBean;
import com.example.wanqing.idles.R;
import com.example.wanqing.model.DealModel;
import com.example.wanqing.model.OnBack;

import java.util.List;

/**
 * Created by dahuahua on 2017/4/27.
 */

public class MyDealActivity extends Activity {
    private MyDealAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deal);

        InitView();
    }

    private void InitView() {
        RecyclerView deal_list = (RecyclerView) findViewById(R.id.my_deal_list);
        adapter = new MyDealAdapter();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);

        deal_list.setAdapter(adapter);
        deal_list.setLayoutManager(manager);
        deal_list.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));

        /*
        *   获取我的购买
        * */
        new Thread(new Runnable() {
            @Override
            public void run() {
                DealModel.QueryForUserDeal(new OnBack<DealBean>() {
                    @Override
                    public void onBack(List<DealBean> list) {
                        if (list != null)
                            adapter.addData(list);
                    }
                });
            }
        }).start();
    }

}
