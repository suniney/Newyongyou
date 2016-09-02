package com.example.yanxu.newyongyou.activity;

import android.content.Context;
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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 * 设置昵称
 */
@EActivity(R.layout.setting_name)
public class NameActivity extends BaseActivity implements HTTPCons {
    @ViewById
    EditText et_name;
    Context mContext;
    @Extra
    String name;
    @Extra
    String regMobile;
    @AfterViews
    void init() {
        mContext = NameActivity.this;
        if (!name.equals(regMobile)) {
            et_name.setText(name);
        }
    }

    @Click
    void btn_next() {
        String name = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(mContext, "昵称不能为空", Toast.LENGTH_SHORT).show();
        } else {
            change(name);
        }
    }

    private void change(final String name) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        map.put("name", name);
        MyOkHttpUtils.okhttpPost(this, modifyName_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    if ("0".equals(response.getErrors().get(0))) {
                        CommonUtils.ToastShow(mContext, "修改成功");
                        SharedPreferencesUtil.writeValueByKey(mContext, "username", name);
                        CommonUtils.sendBroadCast(mContext, "com.yinduo.yongyou.photoupdata", null, null);
                        NameActivity.this.setResult(RESULT_SET);
                        finish();
                    } else {
                        Toast.makeText(mContext, "昵称已存在", Toast.LENGTH_SHORT).show();
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
