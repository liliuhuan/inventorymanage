package com.liliuhuan.com.inventorymanageapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liliuhuan.com.inventorymanageapp.DetailActivity;
import com.liliuhuan.com.inventorymanageapp.UpdateDataActivity;
import com.liliuhuan.com.inventorymanageapp.R;
import com.liliuhuan.com.inventorymanageapp.base.BaseRecycleViewAdapter;
import com.liliuhuan.com.inventorymanageapp.base.BaseRecycleViewHolder;
import com.liliuhuan.com.inventorymanageapp.bean.MainBean;
import com.liliuhuan.com.inventorymanageapp.util.ImageLoader;

import butterknife.Bind;

/**
 * Created by liliuhuan on 2018/2/10.
 */

public class MainAdapter extends BaseRecycleViewAdapter<MainBean> {

    public MainAdapter(Context context) {
        super(context);
    }

    @Override
    public int getConvertViewId(int viewTypeviewType) {
        return R.layout.item_main_layout;
    }

    @Override
    public BaseRecycleViewHolder<MainBean> getNewHolder(View view) {
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends BaseRecycleViewHolder<MainBean> {
        @Bind(R.id.image)
        ImageView mImageView;
        @Bind(R.id.tv_name)
        TextView tvName;
        public MyViewHolder(View view) {
            super(view);
        }

        @Override
        public void loadData(MainBean data, int position) {
            if (data == null) return;

            ImageLoader.loadUriImage(mContext,data.imagePath,mImageView);
            tvName.setText(data.name);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra("pos",position);
                mContext.startActivity(intent);
            });
            itemView.setOnLongClickListener(v -> {
                Intent intent = new Intent(mContext,UpdateDataActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",data);
                bundle.putInt("from",1);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                return true;
            });
        }
    }
}
