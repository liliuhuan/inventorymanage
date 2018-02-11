package com.liliuhuan.com.inventorymanageapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.liliuhuan.com.inventorymanageapp.adapter.ProductAdapter;
import com.liliuhuan.com.inventorymanageapp.bean.ProductBean;
import com.liliuhuan.com.inventorymanageapp.db.RealmHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity implements ProductAdapter.OnChangeListener {

    @Bind(R.id.rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.fl_layout)
    FrameLayout flLayout;
    private RealmHelper realmHelper;
    private ProductAdapter mProductAdapter;
    private List<ProductBean> productBeanList;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras!= null){
            pos = extras.getInt("pos");
        }
        initView();
    }

    private void initView() {
        realmHelper = RealmHelper.getInstance();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProductAdapter = new ProductAdapter(this,pos);
        mRecyclerView.setAdapter(mProductAdapter);
        getData();
        mProductAdapter.setListener(this);
    }
    @Override
    public void changeView() {
        productBeanList = realmHelper.getProductBeanList(pos);
        if (productBeanList == null || productBeanList.size() == 0) {
            flLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            flLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
    private void getData() {
        changeView();
        mProductAdapter.setList(productBeanList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        showDialog();
    }

    private void showDialog() {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.item_product_update_layout, null);
        EditText pName = (EditText) contentView.findViewById(R.id.dialog_p_name);
        EditText pNumber = (EditText) contentView.findViewById(R.id.dialog_p_number);
        Dialog dialog = new AlertDialog.Builder(this)
                .setView(contentView)
                .setNegativeButton("取消", (dialog1, which) -> dialog1.dismiss()).setPositiveButton("确认修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = pName.getText().toString();
                        String number = pNumber.getText().toString();
                        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number)){
                            Toast.makeText(DetailActivity.this,"内容不允许为空！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ProductBean bean ;
                        if (productBeanList == null || productBeanList.size() ==0 ){
                            bean= new ProductBean(pos,0,name,number);
                        }else{
                            bean= new ProductBean(pos,productBeanList.size(),name,number);
                        }
                        realmHelper.insertProductBean(bean);
                        getData();
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCancelable(true);
        dialog.show();
    }


}
