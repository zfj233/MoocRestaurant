package com.zfj.android.moocrestaurant.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zfj.android.moocrestaurant.R;
import com.zfj.android.moocrestaurant.bean.Order;
import com.zfj.android.moocrestaurant.bean.Product;
import com.zfj.android.moocrestaurant.config.Config;
import com.zfj.android.moocrestaurant.utils.T;

import java.util.List;

public class OrderDetailActivity extends BaseActivity {
    private Order mOrder;
    private ImageView mIvImage;
    private TextView mTvTitle;
    private TextView mTvPrice;
    private TextView mTvDesc;
    public static final String KEY_ORDER = "key_order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setUpToolbar();
        setTitle("订单详情");
        initIntent();
        initViews();

    }

    @Override
    public void initViews() {
        mTvTitle = (TextView) findViewById(R.id.id_tv_title);
        mTvDesc = (TextView) findViewById(R.id.id_tv_desc);
        mTvPrice = (TextView) findViewById(R.id.id_tv_price);
        mIvImage = (ImageView) findViewById(R.id.id_iv_image);
        Picasso
                .with(this)
                .load(Config.baseUrl + mOrder.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mIvImage);
        mTvTitle.setText(mOrder.getRestaurant().getName());
        mTvPrice.setText("共消费:"+mOrder.getPrice() + "元");
        List<Order.ProducetVo> ps = mOrder.getPs();
        StringBuilder sb = new StringBuilder();
        for(Order.ProducetVo producetVo :ps){
            sb.append(producetVo.product.getName())
                    .append("*")
                    .append(producetVo.count)
                    .append("\n");
        }
        mTvDesc.setText(sb.toString());
    }

    @Override
    public void initEvents() {

    }

    public static final void launch(Context context, Order order) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(KEY_ORDER, order);
        context.startActivity(intent);
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mOrder = (Order) intent.getSerializableExtra(KEY_ORDER);
        if (mOrder == null) {
            T.showToast("订单未取到");
            return;
        }
    }
}
