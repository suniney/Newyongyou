package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.example.yanxu.newyongyou.view.wheel.ArrayWheelAdapter;
import com.example.yanxu.newyongyou.view.wheel.OnWheelChangedListener;
import com.example.yanxu.newyongyou.view.wheel.WheelView;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 * 添加银行卡
 */
@EActivity(R.layout.setting_addcard)
public class AddCardActivity extends BaseActivity implements HTTPCons, OnWheelChangedListener {
    @ViewById
    LinearLayout wheel;
    @ViewById
    RelativeLayout show_wheel;
    @ViewById
    WheelView mBank;
    @ViewById
    TextView tv_bankName;
    @ViewById
    TextView tv_change;
    @ViewById
    Button btn_sure;
    @ViewById
    EditText et_name;
    @ViewById
    EditText et_num;
    @ViewById
    EditText et_cardname;
    @ViewById
    ImageView bank_icon;
    @Extra
    String type;
    Context mContext;
    protected String[] mBankDatas = new String[]{"中国银行", "中信银行", "中国光大银行", "交通银行", "平安银行",
            "浦发银行", "中国民生银行", "招商银行", "中国建设银行", "中国工商银行", "中国农业银行", "兴业银行"};
    String bankName;

    @AfterViews
    void init() {
        mContext = AddCardActivity.this;
        if ("1".equals(type)) {
            tv_change.setVisibility(View.GONE);
        } else {
            tv_change.setVisibility(View.VISIBLE);
            getBank();
        }
        mBank.setVisibleItems(7);
        mBank.addChangingListener(this);
        mBank.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mBankDatas));
    }

    @Click
    void btn_sure() {
        //姓名开户行银行卡号所在支行
        String name = et_name.getText().toString().trim();
        String bank = tv_bankName.getText().toString().trim();
        String bankNum = et_num.getText().toString().trim();
        String openBank = et_cardname.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(bank)) {
            Toast.makeText(mContext, "请选择银行", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(bankNum)) {
            Toast.makeText(mContext, "请输入银行卡号", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(openBank)) {
            Toast.makeText(mContext, "请输入开户银行", Toast.LENGTH_SHORT).show();
        } else {
            myInfo(name, bank, bankNum, openBank);
        }

    }

    //获取银行卡信息
    private void getBank() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        map.put("status", type);
        MyOkHttpUtils.okhttpPost(mContext, turnBank_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response, int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    WithdrawPage withdrawPage = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), WithdrawPage.class);
                    updata(withdrawPage);
                    AddCardActivity.this.setResult(RESULT_CARD);
                } else {
                    String a = String.valueOf(response.getErrors().get(0));
                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    //提交银行卡
    private void myInfo(String name, String bank, String bankNum, String openBank) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        map.put("name", name);
        map.put("bank", bank);
        map.put("bankNum", bankNum);
        map.put("openBank", openBank);
        MyOkHttpUtils.okhttpPost(mContext, addBankCard_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response, int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    CommonUtils.ToastShow(mContext, "添加银行卡成功");
                    WithdrawPage withdrawPage = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), WithdrawPage.class);
                    updata(withdrawPage);
                } else {
                    String a = String.valueOf(response.getErrors().get(0));
                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    private void updata(WithdrawPage withdrawPage) {
        show_wheel.setVisibility(View.GONE);
        tv_change.setVisibility(View.VISIBLE);
        btn_sure.setVisibility(View.GONE);
        bank_icon.setVisibility(View.VISIBLE);
        tv_bankName.setText(withdrawPage.getBank().toString());
        et_name.setText(withdrawPage.getName());
        et_name.setClickable(false);
        et_name.setEnabled(false);
        et_num.setText(withdrawPage.getBankNum());
        et_num.setClickable(false);
        et_num.setEnabled(false);
        bank_icon.setImageBitmap(BankUtils.getBankIcon(withdrawPage.getBank().toString(), mContext));
        et_cardname.setText(withdrawPage.getOpenBank());
        et_cardname.setClickable(false);
        et_cardname.setEnabled(false);
    }

    @Click
    void tv_change() {
        tv_change.setVisibility(View.GONE);
        btn_sure.setVisibility(View.VISIBLE);
        show_wheel.setVisibility(View.VISIBLE);
        et_name.setClickable(true);
        et_name.setEnabled(true);
        et_num.setClickable(true);
        et_num.setEnabled(true);
        et_cardname.setClickable(true);
        et_cardname.setEnabled(true);
//        AddCardActivity_.intent(this).extra("type", "1").start();
    }


    @Click
    void btn_back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        this.setResult(RESULT_SET);
        this.finish();
    }

    @Click
    void show_wheel() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        wheel.setVisibility(View.VISIBLE);
        bankName = mBankDatas[0];
    }

    @Click
    void btn_close() {
        wheel.setVisibility(View.GONE);
        tv_bankName.setText("");
        bank_icon.setVisibility(View.INVISIBLE);
    }

    @Click
    void btn_ok() {
        bank_icon.setVisibility(View.VISIBLE);
        tv_bankName.setText(bankName);
        bank_icon.setImageBitmap(BankUtils.getBankIcon(bankName, mContext));
        wheel.setVisibility(View.GONE);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        bankName = mBankDatas[newValue];
    }

}
