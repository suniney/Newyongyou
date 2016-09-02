package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.entity.OrderDetailPage;
import com.example.yanxu.newyongyou.entity.OrderReturn;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;
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
 * 预约
 */
@EActivity(R.layout.order)
public class OrderActivity extends BaseActivity implements HTTPCons {

    @ViewById
    EditText et_address;
    @ViewById
    EditText et_phone;
    @ViewById
    EditText et_name;
    @ViewById
    EditText et_money;
    @ViewById
    EditText et_remark;
    @ViewById
    TextView btn_next;
    @Extra
    String proId;
    @Extra
    String orderId;
    @Extra
    String readdrId;
    Context mContext;
    String addrId;
    OrderDetailPage orderDetailPage;

    @AfterViews
    void init() {

        mContext = OrderActivity.this;
        if (orderId == null) {
            getNormal();
        } else {
            getData();
        }
        et_money.addTextChangedListener(new TextWatcher() {
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

    private void getData() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("orderId", orderId);
        map.put("addrId", readdrId);
        MyOkHttpUtils.okhttpPost(mContext, orderDetail_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    orderDetailPage = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), OrderDetailPage.class);
                    et_name.setText(orderDetailPage.getRealRame());
                    et_phone.setText(orderDetailPage.getPhoneNumb());
                    et_money.setText(orderDetailPage.getMoney().toString());
                    if (orderDetailPage.getRealRame() != null) {
                        et_remark.setText(orderDetailPage.getRemark());
                    }
                    et_address.setText(orderDetailPage.getUserAddrs().getAreaName() + orderDetailPage.getUserAddrs().getAddr());
                    addrId = orderDetailPage.getUserAddrs().getId();
                } else {
                    String a = String.valueOf(response.getErrors().get(0));
                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getNormal() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        MyOkHttpUtils.okhttpPost(mContext, turnOrderDetail_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    OrderReturn orderReturn = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), OrderReturn.class);
                    et_phone.setText(orderReturn.getMobile());
                    et_address.setText(orderReturn.getAddr());
                    addrId = orderReturn.getAddrId();
                }
            }

        });
    }

    @Click
    void btn_next() {
        String name = et_name.getText().toString();
        String mobile = et_phone.getText().toString();
        String money = et_money.getText().toString();
        String remark = et_remark.getText().toString();
        String address = et_address.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(mContext, "预约姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CommonUtils.isMobileNO(mobile)) {
            Toast.makeText(mContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(money)) {
            Toast.makeText(mContext, "请输入预约金额", Toast.LENGTH_SHORT).show();
        } else if (!CommonUtils.isCorrectMoney(money)) {
            Toast.makeText(mContext, "输入金额格式错误", Toast.LENGTH_SHORT).show();
        }  else if (addrId == null) {
            Toast.makeText(mContext, "请输入邮寄地址", Toast.LENGTH_SHORT).show();
        } else {
            toOrder(name, mobile, money, remark);
            btn_next.setClickable(false);
        }

    }

    private void toOrder(String name, String mobile, String money, String remark) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        map.put("proId", proId);
        map.put("name", name);
        map.put("mobile", mobile);
        if (!"".equals(remark)) {
            map.put("remark", remark);
        }
        map.put("money", money);
        map.put("addrId", addrId);
        MyOkHttpUtils.okhttpPost(mContext, saveOrderDetail_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                btn_next.setClickable(true);
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    OrderDetailActivity_.intent(mContext).extra("orderId", String.valueOf(response.getErrors().get(0))).extra("addrId", addrId).start();
                    OrderActivity.this.setResult(BaseActivity.RESULT_ORDER);
                    finish();
                } else {
                    String a = String.valueOf(response.getErrors().get(0));
                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Click
    void iv_back() {
        this.finish();
    }

    @Click
    void to_address() {
        MyAddressActivity_.intent(this).extra("from", "order").startForResult(RESULT_ADDRESS);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_ADDRESS:
                Bundle b = data.getExtras(); //data为B中回传的Intent
                String address = b.getString("address");//str即为回传的值
                String area = b.getString("area");
                et_address.setText(area + address);
                addrId = b.getString("addrId");//str即为回传的值
//                Map<String, String> map = new LinkedHashMap<String, String>();
//                map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
//                MyOkHttpUtils.okhttpPost(mContext, turnOrderDetail_action, map, new CommonCallback() {
//                    @Override
//                    public void onResponse(Common response) {
//                        boolean isSucess = response.isSuccess();
//                        if (isSucess) {
//                            OrderReturn orderReturn = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), OrderReturn.class);
//                            et_phone.setText(orderReturn.getMobile());
////                            et_address.setText(orderReturn.getAddr());
////                            addrId = orderReturn.getAddrId();
//                        }
//                    }
//
//                });
//                getNormal();
                break;
            default:
                break;
        }
    }
}
