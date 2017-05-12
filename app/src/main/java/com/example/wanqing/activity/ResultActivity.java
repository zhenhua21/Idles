package com.example.wanqing.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.wanqing.adapter.OnItemClickListener;
import com.example.wanqing.adapter.ResultAdapter;
import com.example.wanqing.bean.IdlesInfoBean;
import com.example.wanqing.idles.R;

import java.util.ArrayList;

/**
 * Created by dahuahua on 2017/5/1.
 */

public class ResultActivity extends Activity {
    ArrayList<IdlesInfoBean> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        this.list.addAll((ArrayList<IdlesInfoBean>) getIntent().getExtras().getSerializable("result"));

        InitRecyclerViewView();
    }

    private void InitRecyclerViewView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.result_list);
        ResultAdapter adapter = new ResultAdapter(getBaseContext(), this.list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);

        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(10));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void ItemClick(View view, int position) {
                Intent intent = new Intent("android.intent.action.IdleDetailsActivity");
                Bundle bundle = new Bundle();
                bundle.putSerializable("idle_details", list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
            }

        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }

}
