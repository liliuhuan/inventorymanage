package com.liliuhuan.com.inventorymanageapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.liliuhuan.com.inventorymanageapp.adapter.MainAdapter;
import com.liliuhuan.com.inventorymanageapp.bean.MainBean;
import com.liliuhuan.com.inventorymanageapp.db.RealmHelper;
import com.liliuhuan.com.inventorymanageapp.evnet.RefreshMainEvent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.btn_add)
    Button btnAdd;
    @Bind(R.id.btn_empty_add)
    Button btnEmptyAdd;
    @Bind(R.id.rv)
    RecyclerView mRecyclerView;
    private MainAdapter mainAdapter;
    private RealmHelper realmHelper;
    private List<MainBean> mainTagsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        realmHelper = RealmHelper.getInstance();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mainAdapter = new MainAdapter(this);
        mRecyclerView.setAdapter(mainAdapter);
        getData();
    }


    private void getData() {
        mainTagsList = realmHelper.getMainTagsList();
        if (mainTagsList == null || mainTagsList.size() == 0) {
            btnEmptyAdd.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            btnAdd.setVisibility(View.GONE);
        } else {
            btnEmptyAdd.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.VISIBLE);
        }
        mainAdapter.setList(mainTagsList);
    }

    public void onEventMainThread(RefreshMainEvent event) {
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.btn_add, R.id.btn_empty_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
            case R.id.btn_empty_add:
                Intent intent = new Intent(this,UpdateDataActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("from",0);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
