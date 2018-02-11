package com.liliuhuan.com.inventorymanageapp.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liliuhuan.com.inventorymanageapp.R;
import com.liliuhuan.com.inventorymanageapp.base.BaseRecycleViewAdapter;
import com.liliuhuan.com.inventorymanageapp.base.BaseRecycleViewHolder;
import com.liliuhuan.com.inventorymanageapp.bean.ProductBean;
import com.liliuhuan.com.inventorymanageapp.db.RealmHelper;

import butterknife.Bind;

/**
 * Created by liliuhuan on 2018/2/10.
 */

public class ProductAdapter extends BaseRecycleViewAdapter<ProductBean> {

    int pos ;
    public ProductAdapter(Context context, int pos) {
        super(context);
        this.pos = pos;
    }

    @Override
    public int getConvertViewId(int viewTypeviewType) {
        return R.layout.item_product_layout;
    }

    @Override
    public BaseRecycleViewHolder<ProductBean> getNewHolder(View view) {
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends BaseRecycleViewHolder<ProductBean> {
        @Bind(R.id.p_name)
        TextView pName;
        @Bind(R.id.des)
        TextView des;
        @Bind(R.id.p_number)
        TextView pNumber;
        @Bind(R.id.btn_delete)
        Button btnDelete;
        @Bind(R.id.btn_update)
        Button btnUpdate;
        public MyViewHolder(View view) {
            super(view);
        }

        @Override
        public void loadData(ProductBean data, int position) {
            if (data == null) return;
            pName.setText(data.name);
            pNumber.setText(data.number);
            btnDelete.setOnClickListener(v -> {
                mDataList.remove(position);
                RealmHelper.getInstance().deleteProductBean(pos,data.sefeid);
                if (mDataList == null || mDataList.size() == 0){
                    if (listener != null)listener.changeView();
                }
                notifyItemChanged(position);

            });
            btnUpdate.setOnClickListener(v -> showDialog(data,position));
        }
        private void showDialog(ProductBean data, int position) {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.item_product_update_layout, null);
            EditText pName = (EditText) contentView.findViewById(R.id.dialog_p_name);
            EditText pNumber = (EditText) contentView.findViewById(R.id.dialog_p_number);
            pName.setText(data.name);
            pNumber.setText(data.number);
            //对话框
            Dialog dialog = new AlertDialog.Builder(mContext)
                    .setView(contentView)
                    .setNegativeButton("取消", (dialog1, which) -> dialog1.dismiss()).setPositiveButton("确认修改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name = pName.getText().toString();
                            String number = pNumber.getText().toString();
                            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number)){
                                Toast.makeText(mContext,"内容不允许为空！",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            RealmHelper.getInstance().updateProductBean(pos,data.id,name,number);
                            data.name = name;
                            data.number = number;
                            notifyItemChanged(position);
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.setCancelable(true);
            dialog.show();
        }
    }


    OnChangeListener listener;

    public void setListener(OnChangeListener listener) {
        this.listener = listener;
    }

    public interface OnChangeListener{
        void changeView();
    }
}
