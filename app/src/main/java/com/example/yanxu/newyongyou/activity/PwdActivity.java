package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;


import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 * 修改密码
 */
@EActivity(R.layout.setting_pwd)
public class PwdActivity extends BaseActivity implements HTTPCons {

    @ViewById
    EditText et_password1;
    @ViewById
    EditText et_password2;
    @ViewById
    EditText et_password3;
    Context mContext;
    @Extra
    String userName;

    @AfterViews
    void init() {
        mContext = PwdActivity.this;
    }

    @Click
    void btn_next() {
        String password1 = et_password1.getText().toString().trim();
        String password2 = et_password2.getText().toString().trim();
        String password3 = et_password3.getText().toString().trim();
        if (TextUtils.isEmpty(password1)) {
            Toast.makeText(mContext, "当前密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password2)) {
            Toast.makeText(mContext, "新密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password3)) {
            Toast.makeText(mContext, "请确认密码", Toast.LENGTH_SHORT).show();
        } else if (!CommonUtils.isCorrectUserPwd(password1)) {
            Toast.makeText(mContext, "当前密码应为6-20位并包含英文或字母", Toast.LENGTH_SHORT).show();
        } else if (!CommonUtils.isCorrectUserPwd(password2)) {
            Toast.makeText(mContext, "新密码应为6-20位并包含英文或字母", Toast.LENGTH_SHORT).show();
        } else if (password1.equals(password2)) {
            Toast.makeText(mContext, "原密码和新密码不能相同", Toast.LENGTH_SHORT).show();
        } else if (!password2.equals(password3)) {
            Toast.makeText(mContext, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
        } else {
            change(password1, password2, password3);
        }
    }

    private void change(String password1, String password2, String password3) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        map.put("password1", password1);
        map.put("password2", password2);
        map.put("password3", password3);
        MyOkHttpUtils.okhttpPost(this, updatePwd_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {

                    if ("1".equals(String.valueOf(response.getErrors().get(0)))) {
                        Toast.makeText(mContext, "原密码错误", Toast.LENGTH_SHORT).show();
                    } else if ("3".equals(String.valueOf(response.getErrors().get(0)))) {
                        Toast.makeText(mContext, "手机号不存在", Toast.LENGTH_SHORT).show();
                    } else {
                        CommonUtils.ToastShow(mContext, "修改成功");
                        SharedPreferencesUtil.writeValueByKey(mContext, "isFirstLogin", false);
                        SharedPreferencesUtil.writeValueByKey(mContext, "userId", "");
                        SharedPreferencesUtil.writeValueByKey(mContext, "mobile", "");
                        File file = new File("/sdcard/avatar.png");
                        if (file.exists()) {
                            file.delete();
                        }
                        CommonUtils.sendBroadCast(mContext, "com.yinduo.yongyou.loginupdata", null, null);
                        LoginActivity_.intent(mContext).extra("userName", userName).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                    }
                }
            }
        });
    }

    @Click
    void btn_back() {
        this.finish();
    }
}
