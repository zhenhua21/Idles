package com.example.wanqing.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wanqing.adapter.BrowseListAdapter;
import com.example.wanqing.idles.R;

import static com.example.wanqing.IdleApplication.UPDATE_IDLE_LIST;

/**
 * Created by dahuahua on 2017/3/9.
 */

/*
* ListFragment是一种Fragment,必须包含一个listview
* tools里面的这个类不对，因为Fragment的arraylist和Listfragment不一样，不是一码事
* 在mainactivity里还得实现一个Fragment的arraylist
* listfragment的实现代码留下来，浏览页面用
* */

public class BrowseListFragment extends ListFragment {
    private static BrowseListAdapter adapter = null;
    private TextView mTextView;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static BrowseListFragment newInstance(String title) {
        BrowseListFragment fragment = new BrowseListFragment();

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listfragment_browse, container, false);

        InitView(view);



        return view;
    }

    private void InitView(View view) {
        adapter = new BrowseListAdapter(getContext(), R.layout.layout_item);
        LinearLayout search = (LinearLayout) view.findViewById(R.id.browse_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.intent.action.SearchActivity"));
            }
        });

        setListAdapter(adapter);
    }

    public static class BrowseListHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_IDLE_LIST) {
                if (adapter != null)
                    adapter.setDate();
            }
        }
    }

}
