package com.zfj.android.moocrestaurant.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zfj.android.moocrestaurant.R;
import com.zfj.android.moocrestaurant.UserInfoHolder;
import com.zfj.android.moocrestaurant.bean.Order;
import com.zfj.android.moocrestaurant.bean.User;
import com.zfj.android.moocrestaurant.biz.OrderBiz;
import com.zfj.android.moocrestaurant.net.CommonCallback;
import com.zfj.android.moocrestaurant.ui.adapter.OrderAdapter;
import com.zfj.android.moocrestaurant.ui.view.CircleTransform;
import com.zfj.android.moocrestaurant.ui.view.refresh.SwipeRefresh;
import com.zfj.android.moocrestaurant.ui.view.refresh.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import com.zfj.android.moocrestaurant.utils.T;

public class OrderActivity extends BaseActivity {
    private Button mBtnOrder;
    private RecyclerView mRecyclerView;
    private TextView mTvUsername;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mIcon;
    private OrderAdapter mAdapter;
    private List<Order> mDatas = new ArrayList<>();
    private OrderBiz mOrderBiz = new OrderBiz();
    private int mCurrentPage = 0;
    private static final String TAG = "OrderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initViews();
        initEvents();
        loadDatas();
    }

    @Override
    public void initViews() {
        mBtnOrder = (Button) findViewById(R.id.id_btn_order);
        mTvUsername = (TextView) findViewById(R.id.id_tv_username);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_refresh);
        mIcon = (ImageView) findViewById(R.id.id_iv_icon);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        User user = UserInfoHolder.getInstance().getUser();
        if (user != null) {
            mTvUsername.setText(user.getUsername());
        } else {
            finish();
            return;
        }
        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.GREEN, Color.YELLOW);

        //recyclerview
        mAdapter = new OrderAdapter(this, mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        Picasso.with(this)
                .load(R.drawable.icon)
                .placeholder(R.drawable.pictures_no)
                .transform(new CircleTransform())
                .into(mIcon);

    }

    @Override
    public void initEvents() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
            }
        });
        mSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });
        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, ProductListActivity.class);
                startActivityForResult(intent, 1001);
            }
        });
    }

    private void loadMore() {
        startLoadingProgress();
        mOrderBiz.listByPage(++mCurrentPage, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mSwipeRefreshLayout.setPullUpRefreshing(false);
                mCurrentPage--;
                if("用户未登录".equals(e.getMessage())){
                    toLoginActivity();
                }
            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                if (response.size() == 0) {
                    T.showToast("已经到底了。");
                    mSwipeRefreshLayout.setPullUpRefreshing(false);

                    return;

                }
                T.showToast("加载成功！");
                mDatas.addAll(response);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setPullUpRefreshing(false);

            }
        });
    }


    private void loadDatas() {
        startLoadingProgress();
        mCurrentPage = 0;
        mOrderBiz.listByPage(mCurrentPage, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                mSwipeRefreshLayout.setRefreshing(false);
                //正常应判断requestCode是否等于-100
                if("用户未登录".equals(e.getMessage())){
                    toLoginActivity();
                }
            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                T.showToast("订单更新成功！");
                mDatas.clear();
                mDatas.addAll(response);
                mAdapter.notifyDataSetChanged();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrderBiz.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            loadDatas();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            try{
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
            }catch (Exception e){

            }

        }
        return super.onKeyDown(keyCode, event);

    }
}
