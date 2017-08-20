package com.zfj.android.moocrestaurant.ui.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zfj.android.moocrestaurant.R;
import com.zfj.android.moocrestaurant.bean.Order;
import com.zfj.android.moocrestaurant.bean.Product;
import com.zfj.android.moocrestaurant.biz.OrderBiz;
import com.zfj.android.moocrestaurant.biz.ProductBiz;
import com.zfj.android.moocrestaurant.net.CommonCallback;
import com.zfj.android.moocrestaurant.ui.adapter.ProductListAdapter;
import com.zfj.android.moocrestaurant.ui.view.refresh.SwipeRefresh;
import com.zfj.android.moocrestaurant.ui.view.refresh.SwipeRefreshLayout;
import com.zfj.android.moocrestaurant.utils.T;
import com.zfj.android.moocrestaurant.vo.ProductItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends BaseActivity {
    private Button mBtnPay;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView mTvCount;
    private ProductBiz mProductBiz = new ProductBiz();
    private OrderBiz mOrderBiz = new OrderBiz();
    private Order mOrder = new Order();
    private ProductListAdapter mAdapter;
    private List<ProductItem> mDatas = new ArrayList<>();
    private int mCurrentPage = 0;
    private double mTotalPrice;
    private int mTotalCount;
    private DecimalFormat df=new DecimalFormat(".##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setUpToolbar();
        setTitle("订餐");
        initViews();
        initEvents();
        loadDatas();
    }

    @Override
    public void initViews() {
        mBtnPay = (Button) findViewById(R.id.id_btn_pay);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_refresh_pro);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_pro);
        mTvCount = (TextView) findViewById(R.id.id_tv_count);
        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.GREEN, Color.YELLOW);

        mAdapter = new ProductListAdapter(this, mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
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

        mBtnPay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mTotalCount <= 0) {
                    T.showToast("您还没有选择菜品。");
                    return;
                }
                mOrder.setCount(mTotalCount);
                mOrder.setPrice(mTotalPrice);
                mOrder.setRestaurant(mDatas.get(0).getRestaurant());
                startLoadingProgress();
                mOrderBiz.add(mOrder, new CommonCallback<String>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String response) {
                        stopLoadingProgress();
                        T.showToast("支付成功！");
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        });

        mAdapter.setOnProductListener(new ProductListAdapter.OnProductListener() {
            @Override
            public void onProductAdd(ProductItem productItem) {
                mTotalCount++;
                mTvCount.setText("数量:" + mTotalCount);
                mTotalPrice += productItem.getPrice();
                mTotalPrice = Double.valueOf(df.format(mTotalPrice));
                mBtnPay.setText(mTotalPrice + "元 立即支付");
                mOrder.addProduct(productItem);
            }

            @Override
            public void onProductSub(ProductItem productItem) {
                mTotalCount--;
                mTvCount.setText("数量:" + mTotalCount);
                mTotalPrice -= productItem.getPrice();
                if (mTotalCount == 0) {
                    mTotalPrice = 0;
                }

                mTotalPrice = Double.valueOf(df.format(mTotalPrice));
                mBtnPay.setText(mTotalPrice + "元 立即支付");
                mOrder.removeProduct(productItem);
            }
        });
    }

    private void loadMore() {
        startLoadingProgress();
        mProductBiz.listByPage(++mCurrentPage, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                mCurrentPage--;
                T.showToast(e.getMessage());
                mSwipeRefreshLayout.setPullUpRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                mSwipeRefreshLayout.setPullUpRefreshing(false);
                if (response.size() <= 0) {
                    T.showToast("已经到底了。");
                    return;
                }
                for (Product p : response) {
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadDatas() {
        startLoadingProgress();
        mCurrentPage = 0;
        mProductBiz.listByPage(mCurrentPage, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                mSwipeRefreshLayout.setRefreshing(false);
                mDatas.clear();
                for (Product p : response) {
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();
                //清空选择的数量，价格
                mTotalCount = 0;
                mTotalPrice = 0;
                mTvCount.setText("数量:" + mTotalCount);
                mBtnPay.setText(mTotalPrice + "元 立即支付");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProductBiz.onDestroy();
    }
}
