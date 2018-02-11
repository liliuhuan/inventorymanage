package com.liliuhuan.com.inventorymanageapp.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.liliuhuan.com.inventorymanageapp.R;

/**
 * Created by liliuhuan on 2018/2/10.
 */

public class ImageLoader {
    /**
     * 加载图片到ImageView，显示圆角
     * @param uri
     * @param imageView
     */
    public static void loadUriImage(Context context,String uri, ImageView imageView){
        if(uri!=null){
            Glide.with(context)
                    .load(uri)
                    .placeholder(R.drawable.ic_lightbulb_outline_green_24dp)
                    .crossFade()
                    .transform(new GlideRoundTransform(context.getApplicationContext()))
                    .into(imageView);
        }

    }
}
