package com.example.yanxu.newyongyou.callBack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author yanxu
 * @date 2016/5/4.
 */
public abstract class ImageCallback extends Callback<Bitmap> {
    @Override
    public Bitmap parseNetworkResponse(Response response, int id) throws Exception {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }
}
