package com.example.wanqing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanqing.adapter.FriendAdapter;
import com.example.wanqing.bean.FriendBean;
import com.example.wanqing.bean.UserBean;
import com.example.wanqing.idles.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by dahuahua on 2017/4/29.
 */

public class FriendActivity extends Activity {
    private FriendAdapter adapter;
//    private ArrayList<FriendBean> list = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView top_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        EventBus.getDefault().register(this);

        addNewFriend();

        mRecyclerView = (RecyclerView) findViewById(R.id.conversation_friends);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.conversation_swipeRefresh);
        top_bar = (TextView) findViewById(R.id.conversation_bar);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new FriendAdapter();

        manager.setOrientation(LinearLayoutManager.VERTICAL);

        top_bar.setText("好友");
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                addNewFriend();
            }
        });

    }

    public void addNewFriend() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<FriendBean> query = new BmobQuery<FriendBean>();
                query.addWhereEqualTo("user", BmobUser.getCurrentUser(UserBean.class));
                query.order("-updatedAt");
                query.findObjects(new FindListener<FriendBean>() {
                    @Override
                    public void done(List<FriendBean> list, BmobException e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (e == null) {

                            adapter.addFriends(new ArrayList<FriendBean>(list));

                            //Log.d("jfksjdfkljads", ": " + list.get(0).getFriend().getUsername() + " /" + list.get(0).getFriend().getNick() + " /" + list.get(0).getFriend().getObjectId());
                        } else {
                            Toast.makeText(getBaseContext(), "没有新的好友", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    /**聊天消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        //处理聊天消息

    }

    /**离线消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event){
        //处理离线消息

    }


}
