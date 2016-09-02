package com.example.yanxu.newyongyou.utils;

import android.content.Context;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Map;

/**
 * Created by yanxu 2016/4/21.
 */
public class MyOkHttpUtils {
    public static void okhttpPost(Context context, String url, Map<String, String> pairs, Callback callback) {
        String sign = MsgDigestUtil.getDigestParam(pairs);
        // make digest sign str
        pairs.put("sign", sign);
        com.zhy.http.okhttp.OkHttpUtils
                .post()
                .url(url)
                .params(pairs)
                .tag(context)
                .build()
                .execute(callback);
    }

    public static void okhttpGet(Context context, String url, Callback callback) {
        com.zhy.http.okhttp.OkHttpUtils.get()
                .url(url)
                .tag(context)
                .build()
                .execute(callback);
    }

    public static void getImage(String url, Callback callback) {
        OkHttpUtils
                .get()//
                .url(url)//
                .build()//
                .execute(callback);
    }

}
