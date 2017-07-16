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
import com.zfj.android.moocrestaurant.bean.Order;
import com.zfj.android.moocrestaurant.config.Config;
import com.zfj.android.moocrestaurant.ui.activity.OrderDetailActivity;

import java.util.List;

/**
 * Created by zfj_ on 2017/7/6.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItemViewHolder> {
    private List<Order> mDatas;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public OrderAdapter(Context context,List<Order> datas) {
        mDatas = datas;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public OrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_order_list, null);
        return new OrderItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderItemViewHolder holder, int position) {
        Order order = mDatas.get(position);
        Picasso.with(mContext)
                .load(Config.baseUrl+ order.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);
        if(order.getPs().size()>0){
            holder.mTvLabel.setText(order
                    .getPs().get(0).product.getName()+"等"+order.getCount()+"件商品");
        }else{
            holder.mTvLabel.setText("无消费");
        }
        holder.mTvName.setText(order.getRestaurant().getName());
        holder.mTvPrice.setText("共消费"+order.getPrice()+"元");
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView mIvImage;
        public TextView mTvName;
        public TextView mTvLabel;
        public TextView mTvPrice;
        public OrderItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //详情页
                    OrderDetailActivity.launch(mContext,
                            mDatas.get(getLayoutPosition()));
                }
            });
            mIvImage = (ImageView) itemView.findViewById(R.id.id_iv_image);
            mTvName = (TextView) itemView.findViewById(R.id.id_tv_name);
            mTvLabel = (TextView) itemView.findViewById(R.id.id_tv_label);
            mTvPrice = (TextView) itemView.findViewById(R.id.id_tv_price);

        }

    }
}
