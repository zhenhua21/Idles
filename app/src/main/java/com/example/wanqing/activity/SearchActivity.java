package com.example.wanqing.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanqing.bean.IdleNameBean;
import com.example.wanqing.bean.IdlesInfoBean;
import com.example.wanqing.bean.UserBean;
import com.example.wanqing.idles.R;
import com.example.wanqing.model.IdleNameModel;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by dahuahua on 2017/4/30.
 */

public class SearchActivity extends Activity implements View.OnClickListener{
    private AutoCompleteTextView input;
    private FlexboxLayout classify;

    private String[] c = {"电子", "书籍", "运动", "书籍", "乐器", "鞋靴", "箱包", "其他"};
    private String[] idle_names = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        InitView();
        dynamicTextView();

    }

    private void InitView() {
        input = (AutoCompleteTextView) findViewById(R.id.search_search);
        classify = (FlexboxLayout) findViewById(R.id.search_classify);

        input.setOnItemClickListener(new OnInputItemClickListener());

        IdleNameModel.QueryIdleName(new IdleNameModel.OnQueryDone() {
            @Override
            public void QueryDone(List<IdleNameBean> list) {
                idle_names = new String[list.size()];

                for (int i = 0; i < list.size(); i++) {
                    idle_names[i] = list.get(i).getIdle_name();
                }

                list.clear();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, idle_names);
                input.setAdapter(adapter);
            }
        });
    }

    /*
    *   动态生成TextView组件
    * */
    private void dynamicTextView() {
        TextView mTextView;

        for (int i = 0; i < 8; i++) {
            mTextView = new TextView(this);
            mTextView.setLayoutParams(new FlexboxLayout.LayoutParams(200, 100));
            mTextView.setOnClickListener(this);
            mTextView.setId(i);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setTextColor(Color.BLUE);
            mTextView.setText(c[i]);

            classify.addView(mTextView);
        }

    }

    /*
    *   取消按钮的点击事件，在xml中声明
    * */
    public void cancel(View view) {
        input.setText("");
    }

    /*
    *   分类查询
    * */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        new Thread(new QueryThread(c[id], 0)).start();
    }

    /*
    *   按商品名称查询
    * */
    private class OnInputItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            new Thread(new QueryThread((String) input.getAdapter().getItem(position), 1)).start();

        }
    }

    /*
    *   查询相应数据
    * */
    private class QueryThread implements Runnable {
        private String condition;
        private int type;

        public QueryThread(String s, int type) {
            this.condition = s;
            this.type = type;
        }

        @Override
        public void run() {
            BmobQuery<IdlesInfoBean> query = new BmobQuery<IdlesInfoBean>();

            if (BmobUser.getCurrentUser() != null)
                query.addWhereNotEqualTo("user_id", BmobUser.getCurrentUser(UserBean.class).getObjectId());
            if (type == 0)  //type等于0表示分类查询
                query.addWhereEqualTo("classify", condition);
            if (type == 1)
                query.addWhereEqualTo("idle_name", condition); //查询username字段的值含有“condition”的数据
            query.order("-updatedAt");  //按照更新时间递减
            query.findObjects(new FindListener<IdlesInfoBean>() {
                @Override
                public void done(List<IdlesInfoBean> list, BmobException e) {
                    if (e == null) {
                        Intent intent = new Intent("android.intent.action.ResultActivity");

                        if (list.size() > 0) {  //将查询到的list传给下个activity
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("result", new ArrayList<IdlesInfoBean>(list));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getBaseContext(), "没有查询到结果", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "查询失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
