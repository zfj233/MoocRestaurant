package com.zfj.android.moocrestaurant.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zfj.android.moocrestaurant.R;
import com.zfj.android.moocrestaurant.bean.User;
import com.zfj.android.moocrestaurant.biz.UserBiz;
import com.zfj.android.moocrestaurant.net.CommonCallback;

import com.zfj.android.moocrestaurant.utils.T;

public class RegisterActivity extends BaseActivity {
    private Button mBtnRegister;
    private EditText mEtPwd;
    private EditText mEtUsrN;
    private EditText mEtRPwd;
    private UserBiz mUserBiz = new UserBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setUpToolbar();
        initViews();
        initEvents();
        setTitle("注册");
    }
    @Override
    public void initEvents() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mEtPwd.getText().toString();
                String username = mEtUsrN.getText().toString();
                final String rePassword = mEtRPwd.getText().toString();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
                    T.showToast("账号或密码不能为空！");
                    return;
                }
                if (!password.equals(rePassword)) {
                    T.showToast("两次密码输入不一致！");
                    return;
                }
                startLoadingProgress();
                mUserBiz.register(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(User response) {
                        stopLoadingProgress();
                        T.showToast("注册成功");
                        //保存用户信息
                        LoginActivity.launch(RegisterActivity.this, response.getUsername(), response.getPassword());
                        finish();
                    }

                });
            }


        });
    }
    @Override
    public void initViews() {
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mEtUsrN = (EditText) findViewById(R.id.id_et_usrn);
        mEtPwd = (EditText) findViewById(R.id.id_et_pwd);
        mEtRPwd = (EditText) findViewById(R.id.id_et_rpwd);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserBiz.onDestroy();
    }
}

