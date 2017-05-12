package com.example.wanqing.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wanqing.adapter.ChatAdapter;
import com.example.wanqing.bean.FriendBean;
import com.example.wanqing.bean.UserBean;
import com.example.wanqing.idles.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dahuahua on 2017/4/27.
 */

public class ChatActivity extends Activity implements View.OnClickListener, ObseverListener, MessageListHandler {
    private static boolean IS_CONNECT = false;
    private BmobIMConversation Conversation;
    private ChatAdapter adapter;
    private RecyclerView mRecyclerView;
    private EditText instant_message;
    private Button send;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayoutManager layoutManager;
    private LinearLayout root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //在聊天页面的onCreate方法中，通过如下方法创建新的会话实例,这个obtain方法才是真正创建一个管理消息发送的会话
        Conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getIntent().getExtras().getSerializable("Conversation"));

        root = (LinearLayout) findViewById(R.id.instant_root);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.instant_swipeRefresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.instant_chat);
        instant_message = (EditText) findViewById(R.id.instant_message);
        send = (Button) findViewById(R.id.instant_send);

        adapter = new ChatAdapter(Conversation);
        layoutManager = new LinearLayoutManager(mRecyclerView.getContext());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSwipeRefreshLayout.setEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new itemDecoration(10));
        mRecyclerView.setAdapter(adapter);

        send.setOnClickListener(this);
        instant_message.setOnClickListener(this);

        //下拉加载
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage msg = adapter.getFirstMessage();
                queryMessages(msg);

            }
        });

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //private Rect r = new Rect();  //Ctrl查看Rect的源代码

            @Override
            public void onGlobalLayout() {
                root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                //获取当前界面的可视部分
//                ChatActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                //获取屏幕的高度
//                int rootHeight = ChatActivity.this.getWindow().getDecorView().getHeight();
//                //获取键盘的高度 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
//                int SoftInputHeight = rootHeight - r.bottom;

                //没有成功... RecyclerView does not support scrolling to an absolute position.
                //mRecyclerView.scrollTo(0, SoftInputHeight);
                //layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
                queryMessages(null);
            }
        });


    }

    /**
     * 消息发送监听器
     */
    public MessageSendListener listener =new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值

            Log.d("onProgress", "onProgress："+value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            adapter.addMessage(msg);
            instant_message.setText("");
            Log.d("instant_message", "instant_message");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
            instant_message.setText("");
            scrollToBottom();
            if (e != null) {
                Log.d("donedone", "done："+e.getErrorCode());
            }
        }
    };

    /**首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg){
        mSwipeRefreshLayout.setRefreshing(false);
        Conversation.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {

                if (e == null) {
                    if (null != list && list.size() > 0) {
                        adapter.addMessages(list);
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
                    Log.d("queryMessages", "错误码: " + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 发送文本消息
     */
    private void sendMessage() {
        String text = instant_message.getText().toString();
        if(TextUtils.isEmpty(text.trim())){
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobIMTextMessage msg =new BmobIMTextMessage();
        msg.setContent(text);

        Conversation.sendMessage(msg, listener);
    }

    /**
     * 显示软键盘
     */
    public void showSoftInputView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(instant_message, 0);
                scrollToBottom();
            }
        }
    }

    /**添加消息到聊天界面中
     * @param event
     */
    private void addMessage2Chat(MessageEvent event){
        BmobIMMessage msg =event.getMessage();
        if(Conversation != null && event!=null && Conversation.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()){//并且不为暂态消息
            if(adapter.findPosition(msg)<0){//如果未添加到界面中
                adapter.addMessage(msg);
                //更新该会话下面的已读状态
                Conversation.updateReceiveStatus(msg);
            }
            scrollToBottom();
        }else{

        }
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage(){
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if(cache.size()>0){
            int size =cache.size();
            for(int i=0;i<size;i++){
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
        Log.d("scrollToBottom", "instant_message");
    }

    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        super.onResume();
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i=0;i<list.size();i++){
            addMessage2Chat(list.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.instant_send) {
            sendMessage();

            new Thread(new NewFriendThread()).start();
        }

        if (v.getId() == R.id.instant_message) {
            showSoftInputView();
        }
    }

    private class NewFriendThread implements Runnable {

        @Override
        public void run() {
            if (getIntent().getBundleExtra("friend") != null) {
                final UserBean f = (UserBean) getIntent().getBundleExtra("friend").getSerializable("friend");

                BmobQuery<FriendBean> query = new BmobQuery<>();
                query.addWhereEqualTo("user", BmobUser.getCurrentUser(UserBean.class));
                query.addWhereEqualTo("friend", f);
                query.findObjects(new FindListener<FriendBean>() {
                    @Override
                    public void done(List<FriendBean> list, BmobException e) {
                        if (e == null && list.size() == 0) {    //如果查询过程没有异常，且查到的行数为0
                            FriendBean Friend = new FriendBean();
                            Friend.setUser(BmobUser.getCurrentUser(UserBean.class));
                            Friend.setFriend(f);
                            Friend.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(getBaseContext(), "添加好友成功", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Friend.setUser(f);
                            Friend.setFriend(BmobUser.getCurrentUser(UserBean.class));
                            Friend.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(getBaseContext(), "添加好友成功", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }

        }
    }

    private class itemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public itemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items

            outRect.top = space;
        }
    }
}
