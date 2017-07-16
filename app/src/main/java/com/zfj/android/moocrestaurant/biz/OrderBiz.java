package com.zfj.android.moocrestaurant.biz;

import com.zfj.android.moocrestaurant.bean.Order;
import com.zfj.android.moocrestaurant.bean.Product;
import com.zfj.android.moocrestaurant.config.Config;
import com.zfj.android.moocrestaurant.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by zfj_ on 2017/7/6.
 */

public class OrderBiz {

    public void listByPage(int currentPage, CommonCallback<List<Order>> commonCallback) {
        OkHttpUtils
                .post()
                .url(Config.baseUrl + "order_find")
                .tag(this)
                .addParams("currentPage", currentPage + "")
                .build()
                .execute(commonCallback);
    }

    public void add(Order order, CommonCallback<String> commonCallback) {
        StringBuilder sb = new StringBuilder();
        Map<Product, Integer> productMap = order.productMap;
        for (Product p : productMap.keySet()) {
            sb.append(p.getId() + "_" + productMap.get(p));
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        OkHttpUtils
                .post()
                .url(Config.baseUrl + "order_add")
                .addParams("res_id", order.getRestaurant().getId() + "")
                .addParams("product_str", sb.toString())
                .addParams("count", order.getCount() + "")
                .addParams("price", order.getPrice() + "")
                .tag(this)
                .build()
                .execute(commonCallback);
    }

    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
