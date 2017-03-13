package com.example.wanqing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.wanqing.idles.R;

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
    private static Context base_context;
    private String title;
    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static BrowseListFragment newInstance(String title, Context context) {
        BrowseListFragment fragment = new BrowseListFragment();

        Bundle args = new Bundle();
        args.putString("title", title);

        base_context = context;
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getArguments().getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listfragment_browse, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(base_context,
                android.R.layout.simple_list_item_1));
    }
}
