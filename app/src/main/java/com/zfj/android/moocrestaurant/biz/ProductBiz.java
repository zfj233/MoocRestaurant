package com.zfj.android.moocrestaurant.biz;

import com.zfj.android.moocrestaurant.bean.Product;
import com.zfj.android.moocrestaurant.config.Config;
import com.zfj.android.moocrestaurant.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

/**
 * Created by zfj_ on 2017/7/8.
 */

public class ProductBiz {
    public void listByPage(int currentPage, CommonCallback<List<Product>> commonCallback) {
        OkHttpUtils
                .post()
                .url(Config.baseUrl + "product_find")
                .addParams("currentPage", currentPage + "")
                .tag(this)
                .build()
                .execute(commonCallback);
    }

    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
