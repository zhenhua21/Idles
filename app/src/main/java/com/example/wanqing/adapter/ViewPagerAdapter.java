package com.example.wanqing.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.wanqing.fragments.BrowseListFragment;
import com.example.wanqing.fragments.PlusFragment;
import com.example.wanqing.fragments.UserFragment;

import java.util.ArrayList;

/**
 * Created by dahuahua on 2017/3/9.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{
    //private Context baseContext;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

    public ViewPagerAdapter(FragmentManager fm, Context baseContext) {
        super(fm);

        mFragments.add(BrowseListFragment.newInstance("Browse"));
        mFragments.add(PlusFragment.newInstance("Plus"));
        mFragments.add(UserFragment.newInstance("User"));

        //this.baseContext = baseContext;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);

    }

    @Override
    public int getCount() {
        return mFragments.size();

    }

}
