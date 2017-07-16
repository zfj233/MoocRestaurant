package com.zfj.android.moocrestaurant.net;

import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

import com.zfj.android.moocrestaurant.utils.GsonUtil;

/**
 * Created by zfj_ on 2017/7/6.
 */

public abstract class CommonCallback<T> extends StringCallback {

    Type mType;

    public CommonCallback() {
        Class<? extends CommonCallback> clazz = getClass();
        Type genericSuperclass = clazz.getGenericSuperclass();
        if(genericSuperclass instanceof Class){
            throw new RuntimeException("Miss Type Params");
        }
        ParameterizedType parameterizedType =
                (ParameterizedType) genericSuperclass;
        //get T
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        mType = actualTypeArguments[0];
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onError(e);
    }

    @Override
    public void onResponse(String response, int id) {
        JSONObject resp = null;
        try {
            resp = new JSONObject(response);
            int resultCode = resp.getInt("resultCode");
            if(resultCode == 1){
                //success
                String data = resp.getString("data");
                onSuccess((T) GsonUtil.getGson().fromJson(data,mType));

            }else{
                onError(new RuntimeException(resp.getString("resultMessage")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public abstract void onError(Exception e);
    public abstract void onSuccess(T response);
}
