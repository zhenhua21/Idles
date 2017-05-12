package com.example.wanqing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wanqing.adapter.CommentAdapter;
import com.example.wanqing.adapter.RollImageAdapter;
import com.example.wanqing.bean.CommentBean;
import com.example.wanqing.bean.IdlesInfoBean;
import com.example.wanqing.bean.UserBean;
import com.example.wanqing.idles.R;
import com.example.wanqing.model.CommentModel;
import com.example.wanqing.model.IdleInfoModel;
import com.example.wanqing.model.OnBack;
import com.example.wanqing.model.OnResultBack;
import com.jude.rollviewpager.RollPagerView;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by dahuahua on 2017/4/27.
 */

public class IdleDetailsActivity extends Activity implements View.OnClickListener {
    private IdlesInfoBean mIdlesInfoBean = null;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle_details);

        mIdlesInfoBean = (IdlesInfoBean) getIntent().getExtras().getSerializable("idle_details");

        InitView();
        InitIdle();
        InitComment();
        downUserInfo(mIdlesInfoBean.getUser_id());
    }

    private void InitView() {
        TextView iwant = (TextView) findViewById(R.id.details_iwant);
        TextView buy = (TextView) findViewById(R.id.details_buy);
        ImageView like = (ImageView) findViewById(R.id.details_like);
        ImageView evaluation = (ImageView) findViewById(R.id.details_evaluation);
        Button take_evaluation = (Button) findViewById(R.id.details_take_evaluation);

        if (mIdlesInfoBean.getIsSold() == 0)
            buy.setOnClickListener(this);
        if (mIdlesInfoBean.getIsSold() == 1) {
            buy.setText("你来晚了");
            buy.setTextColor(getResources().getColor(R.color.colorAccent));
            buy.setBackgroundResource(R.color.colorGray);
        }

        iwant.setOnClickListener(this);
        like.setOnClickListener(this);
        evaluation.setOnClickListener(this);
        take_evaluation.setOnClickListener(this);
    }

    /*
    *   初始化商品信息
    * */
    private void InitIdle() {
        RollPagerView mRollPagerView = (RollPagerView) findViewById(R.id.details_roll_img);
        TextView idle_name = (TextView) findViewById(R.id.details_idle_name);
        TextView price = (TextView) findViewById(R.id.details_price);
        TextView phone = (TextView) findViewById(R.id.details_phone);
        TextView classify = (TextView) findViewById(R.id.details_classify);
        TextView content = (TextView) findViewById(R.id.details_content);
        RollImageAdapter adapter = new RollImageAdapter(this, mIdlesInfoBean.getPictures());

        idle_name.setText(mIdlesInfoBean.getIdle_name());
        price.setText(mIdlesInfoBean.getPrice() + "元");
        phone.setText(mIdlesInfoBean.getPhone());
        classify.setText(mIdlesInfoBean.getClassify());
        content.setText(mIdlesInfoBean.getContent());
        mRollPagerView.setAdapter(adapter);
    }

    private void InitComment() {
        RecyclerView comment = (RecyclerView) findViewById(R.id.details_show_evaluation);
        adapter = new CommentAdapter();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);

        comment.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));
        comment.setLayoutManager(manager);
        comment.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CommentModel.QueryForIdleComment(mIdlesInfoBean, new OnBack<CommentBean>() {
                    @Override
                    public void onBack(List<CommentBean> list) {
                        if (list != null)
                            adapter.addData(list);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        LinearLayout eva = (LinearLayout) findViewById(R.id.details_make_evaluation);
        FrameLayout bottom = (FrameLayout) findViewById(R.id.details_bottom);
        final EditText info_evaluation = (EditText) findViewById(R.id.details_info_evaluation);

        switch (v.getId()) {
            case R.id.details_iwant:
                if (BmobUser.getCurrentUser() != null) {
                    //开启一个会话
                    new Thread(new OpenConversationThread()).start();
                }else {
                    startActivity(new Intent("android.intent.action.LoginOrRegisterActivity"));
                }

                break;
            case R.id.details_buy:
                if (BmobUser.getCurrentUser() != null) {
                    Intent intent = new Intent("android.intent.action.DealActivity");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("idle", mIdlesInfoBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    startActivity(new Intent("android.intent.action.LoginOrRegisterActivity"));
                }

                break;
            case R.id.details_like:
                Log.d("details_like", "details_like");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        IdleInfoModel.AddLikes(mIdlesInfoBean, new OnResultBack() {
                            @Override
                            public void onResultBack() {
                                mIdlesInfoBean.setLikes(mIdlesInfoBean.getLikes() + 1);
                                Toast.makeText(getBaseContext(), "点赞", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();

                break;
            case R.id.details_evaluation:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                eva.setVisibility(View.VISIBLE);
                bottom.setVisibility(View.GONE);
                info_evaluation.requestFocus();
                break;
            case R.id.details_take_evaluation:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                eva.setVisibility(View.GONE);
                bottom.setVisibility(View.VISIBLE);
                final String info = info_evaluation.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CommentModel.InsertComment(info, mIdlesInfoBean, new OnBack<CommentBean>() {
                            @Override
                            public void onBack(List<CommentBean> list) {
                                //在这里给留言adapter添加一条新数据
                                if (list != null) {
                                    adapter.addData(list);
                                }
                            }
                        });
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    /*
   *   设置用户信息
   * */
    private void downUserInfo(final String objectId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<UserBean> query = new BmobQuery<>();
                query.getObject(objectId, new QueryListener<UserBean>() {
                    @Override
                    public void done(UserBean userBean, BmobException e) {
                        if (e == null) {
                            TextView username = (TextView) findViewById(R.id.details_user_name);
                            ImageView userimg = (ImageView) findViewById(R.id.details_img);

                            username.setText(userBean.getUsername());
                            Glide.with(getBaseContext())
                                    .load(userBean.getNick())
                                    .into(userimg);
                        }
                    }
                });
            }
        }).start();

    }

    //开启聊天
    private class OpenConversationThread implements Runnable {

        @Override
        public void run() {
            BmobQuery<UserBean> query = new BmobQuery<>();
            query.addWhereEqualTo("objectId", mIdlesInfoBean.getUser_id());
            query.findObjects(new FindListener<UserBean>() {
                @Override
                public void done(List<UserBean> list, BmobException e) {
                    if (e == null) {
                        final UserBean friend = list.get(0);

                        //创建私聊会话，默认会保存该会话到自己的本地会话表中
                        BmobIMUserInfo userInfo = new BmobIMUserInfo(friend.getObjectId(), friend.getUsername(), friend.getNick());
                        BmobIM.getInstance().startPrivateConversation(userInfo, new ConversationListener() {
                            @Override
                            public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                                if (e == null) {
                                    //在此跳转到聊天页面
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Conversation", bmobIMConversation);
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putSerializable("friend", friend);
                                    Intent intent = new Intent("android.intent.action.ChatActivity");
                                    intent.putExtras(bundle);
                                    intent.putExtra("friend", bundle1);
                                    startActivity(intent);
                                }else {
                                    Log.d("PrivateConversation", e.getErrorCode() + " / " + e.getMessage());
                                }
                            }
                        });
                    }
                }
            });
        }
    }


}
