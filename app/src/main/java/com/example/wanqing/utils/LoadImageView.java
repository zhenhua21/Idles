package com.example.wanqing.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by dahuahua on 2017/4/29.
 */

public class LoadImageView {
    public static void LoadImageView(ImageView imageView, String url, int defaultRes) {
        if (url == null) {
            imageView.setImageResource(defaultRes);
            return;
        }

        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }
}
