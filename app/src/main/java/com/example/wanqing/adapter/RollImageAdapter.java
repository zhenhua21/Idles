package com.example.wanqing.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wanqing.utils.LoadImageView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dahuahua on 2017/5/3.
 */

public class RollImageAdapter extends StaticPagerAdapter {
    private Context context;
    private ArrayList<String> pictures = new ArrayList<>();

    public RollImageAdapter(Context context, List<String> picture) {
        this.context = context;
        pictures.addAll(picture);

    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Glide.with(context)
                .load(pictures.get(position))
                .into(view);

        return view;
    }

    @Override
    public int getCount() {
        return pictures.size();
    }
}
