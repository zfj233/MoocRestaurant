package com.zfj.android.moocrestaurant.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zfj.android.moocrestaurant.R;
import com.zfj.android.moocrestaurant.bean.Product;
import com.zfj.android.moocrestaurant.config.Config;
import com.zfj.android.moocrestaurant.ui.activity.ProductDetailActivity;
import com.zfj.android.moocrestaurant.vo.ProductItem;

import java.util.List;

/**
 * Created by zfj_ on 2017/7/8.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {
    private List<ProductItem> mProductItems;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnProductListener mListener;


    public ProductListAdapter(Context context, List<ProductItem> productItems) {
        mProductItems = productItems;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_product_list, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductItem productItem = mProductItems.get(position);
        Picasso
                .with(mContext)
                .load(Config.baseUrl + productItem.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);
        holder.mTvName.setText(productItem.getName());
        holder.mTvCount.setText(productItem.count + "");
        holder.mTvLabel.setText(productItem.getLabel());
        holder.mTvPrice.setText(productItem.getPrice() + "元/份");
    }

    @Override
    public int getItemCount() {
        return mProductItems.size();
    }


    public interface OnProductListener {
        void onProductAdd(ProductItem productItem);

        void onProductSub(ProductItem productItem);

    }

    public void setOnProductListener(OnProductListener listener) {
        mListener = listener;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvImage;
        public ImageView mIvAdd;
        public ImageView mIvSub;
        public TextView mTvName;
        public TextView mTvCount;
        public TextView mTvLabel;
        public TextView mTvPrice;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mIvImage = (ImageView) itemView.findViewById(R.id.id_iv_image);
            mIvAdd = (ImageView) itemView.findViewById(R.id.id_iv_add);
            mIvSub = (ImageView) itemView.findViewById(R.id.id_iv_sub);
            mTvCount = (TextView) itemView.findViewById(R.id.id_tv_count_item);
            mTvLabel = (TextView) itemView.findViewById(R.id.id_tv_label);
            mTvName = (TextView) itemView.findViewById(R.id.id_tv_name);
            mTvPrice = (TextView) itemView.findViewById(R.id.id_tv_price);
            //TODO 商品详情页 应该回调的。。
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDetailActivity.launch(mContext,
                            mProductItems.get(getLayoutPosition()));
                }
            });

            mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = mProductItems.get(pos);
                    productItem.count++;
                    mTvCount.setText(productItem.count + "");
                    if (mListener != null) {
                        mListener.onProductAdd(productItem);
                    }
                }
            });

            mIvSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = mProductItems.get(pos);
                    if (productItem.count <= 0) {
                        return;
                    }
                    productItem.count--;
                    mTvCount.setText(productItem.count + "");
                    if (mListener != null) {
                        mListener.onProductSub(productItem);
                    }
                }
            });
        }
    }
}
