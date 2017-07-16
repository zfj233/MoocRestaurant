package com.zfj.android.moocrestaurant.vo;

import com.zfj.android.moocrestaurant.bean.Product;

import java.io.Serializable;

/**
 * Created by zfj_ on 2017/7/8.
 */

public class ProductItem extends Product {
    public int count;

    public ProductItem(Product product) {
        this.count = 0;
        this.id = product.getId();
        this.name = product.getName();
        this.label = product.getLabel();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.icon = product.getIcon();
        this.restaurant = product.getRestaurant();
    }
}
