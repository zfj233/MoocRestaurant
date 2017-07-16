package com.zfj.android.moocrestaurant.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zfj.android.moocrestaurant.R;
import com.zfj.android.moocrestaurant.bean.Product;
import com.zfj.android.moocrestaurant.config.Config;
import com.zfj.android.moocrestaurant.utils.T;
import com.zfj.android.moocrestaurant.vo.ProductItem;

public class ProductDetailActivity extends BaseActivity {
    private Product mProduct;
    private ImageView mIvImage;
    private TextView mTvTitle;
    private TextView mTvPrice;
    private TextView mTvDesc;
    public static final String KEY_PRODUCT = "key_product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setUpToolbar();
        setTitle("详情");
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
                .load(Config.baseUrl + mProduct.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mIvImage);
        mTvTitle.setText(mProduct.getName());
        mTvPrice.setText(mProduct.getPrice() + "元/份");
        mTvDesc.setText(mProduct.getDescription());
    }

    @Override
    public void initEvents() {

    }

    public static final void launch(Context context, Product product) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT, product);
        context.startActivity(intent);
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mProduct = (Product) intent.getSerializableExtra(KEY_PRODUCT);
        if (mProduct == null) {
            T.showToast("商品未取到");
            return;
        }
    }
}
