package com.example.wanqing.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.example.wanqing.adapter.ViewPagerAdapter;
import com.example.wanqing.idles.R;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private Button mBrowse, mPlus, mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize();

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new PagerChangeListener());

        mBrowse.setOnClickListener(new ButtonOnClickListener(0));
        mPlus.setOnClickListener(new ButtonOnClickListener(1));
        mUser.setOnClickListener(new ButtonOnClickListener(2));
    }

    private void Initialize() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getBaseContext());

        mViewPager = (ViewPager) findViewById(R.id.vp_content);

        mBrowse = (Button) findViewById(R.id.main_bt_browse);
        mPlus = (Button) findViewById(R.id.main_bt_plus);
        mUser = (Button) findViewById(R.id.main_bt_user);

    }

    public class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mBrowse.getPaint().setFakeBoldText(true);
                    mBrowse.setTextColor(getResources().getColor(R.color.colorTextSelected));
                    mPlus.getPaint().setFakeBoldText(false);
                    mPlus.setTextColor(getResources().getColor(R.color.colorTextSelecting));
                    mUser.getPaint().setFakeBoldText(false);
                    mUser.setTextColor(getResources().getColor(R.color.colorTextSelecting));
                    break;
                case 1:
                    mPlus.getPaint().setFakeBoldText(true);
                    mPlus.setTextColor(getResources().getColor(R.color.colorTextSelected));
                    mBrowse.getPaint().setFakeBoldText(false);
                    mBrowse.setTextColor(getResources().getColor(R.color.colorTextSelecting));
                    mUser.getPaint().setFakeBoldText(false);
                    mUser.setTextColor(getResources().getColor(R.color.colorTextSelecting));
                    break;
                case 2:
                    mUser.getPaint().setFakeBoldText(true);
                    mUser.setTextColor(getResources().getColor(R.color.colorTextSelected));
                    mBrowse.getPaint().setFakeBoldText(false);
                    mBrowse.setTextColor(getResources().getColor(R.color.colorTextSelecting));
                    mPlus.getPaint().setFakeBoldText(false);
                    mPlus.setTextColor(getResources().getColor(R.color.colorTextSelecting));
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    }

    public class ButtonOnClickListener implements View.OnClickListener {
        private int item;

        public ButtonOnClickListener(int item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(item);
        }
    }

}
