package com.example.yanxu.newyongyou.callBack;

import android.util.Log;

import com.example.yanxu.newyongyou.entity.Common;
import com.google.gson.Gson;

import okhttp3.Response;

/**
 * Created by yanxu 2016/4/1.
 */
public abstract class CommonCallback extends BaseCallback<Common> {
    @Override
    public Common parseNetworkResponse(Response response, int id) throws Exception {
        Log.d("response", response.message());
        String string = response.body().string();
        Common common = new Gson().fromJson(string, Common.class);
        Log.d("response", string);
        return common;
    }
}



