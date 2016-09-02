package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.entity.Money;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by yanxu 2016/4/1.
 * 累计佣金
 */
@EActivity(R.layout.setting_money)
public class MoneyActivity extends BaseActivity implements HTTPCons {
    @ViewById
    TextView tv_money;
    @ViewById
    TextView withdraw_money;
    @ViewById
    Button withdraw_money_right;
    Context mContext;
    Money money;
    BigDecimal withdraw;

    @AfterViews
    void init() {
        mContext = MoneyActivity.this;
        getData();
    }


    private void getData() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        MyOkHttpUtils.okhttpPost(this, balancePage_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    money = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), Money.class);
                    if (money.getAllIncome() == null) {
                        tv_money.setText("￥0.00");
                    } else {
                        tv_money.setText(getString(R.string.money1, money.getAllIncome()));
                    }
                    if (money.getBalance() == null) {
                        withdraw_money.setText("￥0.00");
                        withdraw = new BigDecimal("0.00");
                    } else {
                        withdraw_money.setText(getString(R.string.money1, money.getBalance()));
                        withdraw = money.getBalance();
                    }
                } else {
                    String a = String.valueOf(response.getErrors().get(0));
                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void withdraw_money_right() {
        WithdrawActivity_.intent(this).extra("withdraw", withdraw).startForResult(RESULT_WITHDRAW);
    }

    @OnActivityResult(RESULT_WITHDRAW)
    void updata() {
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }


}
