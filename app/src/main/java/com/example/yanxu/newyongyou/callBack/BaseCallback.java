package com.example.yanxu.newyongyou.callBack;

import android.util.Log;

import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by yanxu 2016/4/1.
 */
public abstract class BaseCallback<L> extends Callback<L> {
    @Override
    public void onError(Call call, Exception e, int id) {
        Log.d("onError", e.toString());
    }

    @Override
    public L parseNetworkResponse(Response response, int id) throws Exception {
        return null;
    }


}



