package com.zfj.android.moocrestaurant.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zfj.android.moocrestaurant.R;

/**
 * Created by zfj_ on 2017/7/6.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mLoadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置顶部的颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(0xff000000);
        }
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setMessage("加载中");

    }
    //初始化 View
    public abstract void initViews();
    //初始化事件
    public abstract void initEvents();
    protected void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    protected void stopLoadingProgress() {
        if(mLoadingDialog!=null&&mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
    }

    protected void startLoadingProgress() {
        mLoadingDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoadingProgress();
        mLoadingDialog = null;
    }
    //To登录界面

    protected void toLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
