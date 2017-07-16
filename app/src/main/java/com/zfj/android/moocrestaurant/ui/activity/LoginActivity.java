package com.zfj.android.moocrestaurant.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zfj.android.moocrestaurant.R;
import com.zfj.android.moocrestaurant.UserInfoHolder;
import com.zfj.android.moocrestaurant.bean.User;
import com.zfj.android.moocrestaurant.biz.UserBiz;
import com.zfj.android.moocrestaurant.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

import com.zfj.android.moocrestaurant.utils.T;

public class LoginActivity extends BaseActivity {
    private UserBiz mUserBiz = new UserBiz();
    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private TextView mTvRegister;
    public static String KEY_USERNAME = "username";
    public static String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initEvents();
        initIntent(getIntent());
    }
    @Override
    public void initViews() {
        mEtUsername = (EditText) findViewById(R.id.id_et_username);
        mEtPassword = (EditText) findViewById(R.id.id_et_password);
        mBtnLogin = (Button) findViewById(R.id.id_btn_login);
        mTvRegister = (TextView) findViewById(R.id.id_tv_register);
    }
    @Override
    public void initEvents() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Login?
                String password = mEtPassword.getText().toString();
                String username = mEtUsername.getText().toString();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
                    T.showToast("账号或密码不能为空！");
                    return;
                }
                startLoadingProgress();
                mUserBiz.login(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(User user) {
                        stopLoadingProgress();
                        T.showToast("登录成功");
                        //保存用户信息
                        UserInfoHolder.getInstance().setUser(user);
                        toOrderActvity();
                    }

                });
            }
        });
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegisterActivity();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);
    }

    private void initIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        String username = intent.getStringExtra(KEY_USERNAME);
        String password = intent.getStringExtra(KEY_PASSWORD);
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        }
        mEtUsername.setText(username);
        mEtPassword.setText(password);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieJarImpl cookieJar = (CookieJarImpl) OkHttpUtils.getInstance()
                .getOkHttpClient().cookieJar();
        cookieJar.getCookieStore().removeAll();
    }

    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void toOrderActvity() {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
        finish();
    }

    public static void launch(Context context, String username, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KEY_USERNAME, username);
        intent.putExtra(KEY_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserBiz.onDestroy();
    }
}
