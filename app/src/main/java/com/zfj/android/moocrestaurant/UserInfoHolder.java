package com.zfj.android.moocrestaurant;

import android.text.TextUtils;

import com.zfj.android.moocrestaurant.bean.User;

import com.zfj.android.moocrestaurant.utils.SPUtils;

/**
 * Created by zfj_ on 2017/7/6.
 */

public class UserInfoHolder {
    private static UserInfoHolder mInstance = new UserInfoHolder();
    private User mUser;
    public static final String KEY_USERNAME = "key_username";

    public static UserInfoHolder getInstance() {
        return mInstance;
    }

    public User getUser() {
        User u = mUser;
        if (u == null) {
            String username = (String) SPUtils.getInstance().get(KEY_USERNAME, "");
            if (!TextUtils.isEmpty(username)) {
                u = new User();
                u.setUsername(username);
            }
        }
        mUser = u;
        return u;
    }

    public void setUser(User user) {
        mUser = user;
        if (user == null) {
            SPUtils.getInstance().put(KEY_USERNAME, user.getUsername());
        }
    }


}
