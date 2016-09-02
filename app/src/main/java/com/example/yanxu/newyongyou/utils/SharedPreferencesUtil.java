package com.example.yanxu.newyongyou.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {

    public static final String SHARE_NAME = "ydzb_yongyou_prf";             // 文件名称

    public static boolean getValueByKey(Context context, String key, boolean defaultVal) {
        SharedPreferences preferences = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defaultVal);
    }

    public static String getValueByKey(Context context, String key, String defaultVal) {
        SharedPreferences preferences = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultVal);
    }


    public static void writeValueByKey(Context context, String key, boolean value) {
        SharedPreferences preferneces = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        Editor editor = preferneces.edit();
        editor.putBoolean(key, value); // 登录状态第一次登录后设置为false保存
        editor.commit();
    }

    public static void writeValueByKey(Context context, String key, String value) {
        SharedPreferences preferneces = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        Editor editor = preferneces.edit();
        editor.putString(key, value); // 登录状态第一次登录后设置为false保存
        editor.commit();
    }

}