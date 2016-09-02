package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.entity.WithdrawPage;
import com.example.yanxu.newyongyou.utils.BankUtils;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 * 提现
 */
@EActivity(R.layout.setting_withdraw)
public class WithdrawActivity extends BaseActivity implements HTTPCons {
    Context mContext;
    @ViewById
    ImageView bank_icon;
    @ViewById
    TextView user_name;
    @ViewById
    TextView bank_name;
    @ViewById
    TextView bank_num;
    @ViewById
    TextView bank_open;
    @ViewById
    EditText withDraw_money;
    @ViewById
    RelativeLayout add_card;
    @ViewById
    LinearLayout card_detail;
    WithdrawPage withdrawPage;
    @Extra
    BigDecimal withdraw;

    @AfterViews
    void init() {
        withDraw_money.setHint("最多可提现" + withdraw);
        mContext = WithdrawActivity.this;
        getData();
//设定小数点后两位
        withDraw_money.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }


            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }

    @Click
    void btn_next() {
        String cashvalue = withDraw_money.getText().toString().trim();
        if (withdrawPage == null) {
            Toast.makeText(mContext, "请添加银行卡", Toast.LENGTH_SHORT).show();
        } else if ("".equals(cashvalue)) {
            Toast.makeText(mContext, "请输入提现金额", Toast.LENGTH_SHORT).show();
        } else if (".".equals(cashvalue)) {
            Toast.makeText(mContext, "请输入正确金额", Toast.LENGTH_SHORT).show();
        } else if (!CommonUtils.isCorrectMoney(cashvalue)) {
            Toast.makeText(mContext, "输入金额格式错误", Toast.LENGTH_SHORT).show();
        } else {
            BigDecimal amoney = new BigDecimal(cashvalue);
            BigDecimal nomoney = new BigDecimal("0");
//返回值    -1 小于   0 等于    1 大于
            if (amoney.compareTo(withdraw) == 1) {
                Toast.makeText(mContext, "提现金额不足", Toast.LENGTH_SHORT).show();
            } else if (amoney.compareTo(nomoney) == 0) {
                Toast.makeText(mContext, "请输入正确金额", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(cashvalue)) {
                Toast.makeText(mContext, "请输入提现金额", Toast.LENGTH_SHORT).show();
            } else {
                withDraw(cashvalue);
            }
        }

    }

    @Click
    void btn_add() {
        AddCardActivity_.intent(mContext).extra("type", "1").startForResult(RESULT_CARD);
    }

    @OnActivityResult(RESULT_CARD)
    void updata() {
        getData();
    }

    private void getData() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        MyOkHttpUtils.okhttpPost(this, withdrawPage_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {

                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    withdrawPage = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), WithdrawPage.class);
                    if (withdrawPage == null) {
                        add_card.setVisibility(View.VISIBLE);
                        card_detail.setVisibility(View.GONE);
                    } else {
                        add_card.setVisibility(View.GONE);
                        card_detail.setVisibility(View.VISIBLE);
                        user_name.setText(withdrawPage.getName());
                        bank_name.setText(withdrawPage.getBank());
                        bank_num.setText(String.valueOf(withdrawPage.getBankNum()));
                        bank_icon.setImageBitmap(BankUtils.getBankIcon(withdrawPage.getBank().toString(), mContext));
                        bank_open.setText(withdrawPage.getOpenBank());
                    }
                } else {
                    String a = String.valueOf(response.getErrors().get(0));

                }
            }
        });
    }

    private void withDraw(String cashvalue) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        map.put("realName", withdrawPage.getName());
        map.put("bankId", withdrawPage.getBankId().toString());
        map.put("cardNumber", String.valueOf(withdrawPage.getBankNum()));
        map.put("openBank", withdrawPage.getOpenBank());
        map.put("cashvalue", cashvalue);
        MyOkHttpUtils.okhttpPost(mContext, withdraw_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    CommonUtils.ToastShow(mContext, "提现成功");
                    WithdrawActivity.this.setResult(RESULT_WITHDRAW);
                    finish();
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


}
