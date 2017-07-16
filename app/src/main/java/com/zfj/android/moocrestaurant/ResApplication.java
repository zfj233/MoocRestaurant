package com.zfj.android.moocrestaurant;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import com.zfj.android.moocrestaurant.utils.SPUtils;
import com.zfj.android.moocrestaurant.utils.T;

/**
 * Created by zfj_ on 2017/7/6.
 */

public class ResApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        T.init(this);
        SPUtils.init(this,"sp_user.pref");
        CookieJarImpl cookieJar = new CookieJarImpl(
                new PersistentCookieStore(getApplicationContext())
        );
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
}
