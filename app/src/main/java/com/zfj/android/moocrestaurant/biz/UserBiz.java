package com.zfj.android.moocrestaurant.biz;

import com.zfj.android.moocrestaurant.bean.User;
import com.zfj.android.moocrestaurant.config.Config;
import com.zfj.android.moocrestaurant.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * Created by zfj_ on 2017/7/6.
 */

public class UserBiz {
    public void login(String username, String password,
                      CommonCallback<User> commonCallback){
        OkHttpUtils
                .post()
                .url(Config.baseUrl+"user_login")
                .tag(this)
                .addParams("username",username)
                .addParams("password",password)
                .build()
                .execute(commonCallback);
    }
    public void register(String username, String password,
                         CommonCallback<User> commonCallback){
        OkHttpUtils
                .post()
                .url(Config.baseUrl+"user_register")
                .tag(this)
                .addParams("username",username)
                .addParams("password",password)
                .build()
                .execute(commonCallback);
    }
    public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
