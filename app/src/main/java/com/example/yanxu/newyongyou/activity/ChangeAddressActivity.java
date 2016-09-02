package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Address;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;
import com.example.yanxu.newyongyou.utils.UISwitchButton;
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
 * 更改地址
 */
@EActivity(R.layout.setting_address_change)
public class ChangeAddressActivity extends AddressActivity implements HTTPCons, OnWheelChangedListener {
    @ViewById
    WheelView mViewProvince;
    @ViewById
    WheelView mViewCity;
    @ViewById
    WheelView mViewDistrict;
    @ViewById
    Button btn_ok;
    @ViewById
    Button btn_close;
    @ViewById
    LinearLayout wheelview;
    @ViewById
    RelativeLayout show_wheel;
    @ViewById
    TextView tv_address;
    @ViewById
    EditText et_phone;
    @ViewById
    EditText et_address;
    @ViewById
    EditText et_name;
    private Context mContext;
    @ViewById
    UISwitchButton address_btn;
    @ViewById
    TextView title;
    @Extra
    String addrId;
    @Extra
    String mytitle;
    Address address;

    @AfterViews
    void init() {
        mContext = ChangeAddressActivity.this;
        title.setText(mytitle);
        getAddress();
        setUpData();
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
    }

    @Click
    void btn_next() {
        String realName = et_name.getText().toString().trim();
        String mobile = et_phone.getText().toString().trim();
        String areaName = tv_address.getText().toString().trim();
        String addr = et_address.getText().toString().trim();
        changeAddress(mobile, realName, addr, areaName);
    }

    private void getAddress() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("addrId", addrId);
        MyOkHttpUtils.okhttpPost(this, searchAddr_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response, int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    address = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), Address.class);
                    et_address.setText(address.getAddr());
                    et_name.setText(address.getRealName());
                    et_phone.setText(address.getMobile());
                    tv_address.setText(address.getAreaName());
                }
            }

        });
    }

    private void changeAddress(String mobile, String realName, String addr, String areaName) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("mobile", mobile);
        map.put("addrId", addrId);
        map.put("realName", realName);
        map.put("addr", addr);
        map.put("areaName", areaName);
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        MyOkHttpUtils.okhttpPost(this, updatePostAddr_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response, int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                    ChangeAddressActivity.this.setResult(RESULT_ORDER);
                    CommonUtils.sendBroadCast(mContext, "com.yinduo.yongyou.addrupdata", null, null);
                    finish();
                }
            }


        });
    }

    @Click
    void rl_phone() {
//        Intent intent = new Intent();
//        intent.setClass(this, AddPhoneActivity.class);
//        startActivityForResult(intent, RESULT_PHONE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_PHONE:
                if (data!=null){
                Bundle b = data.getExtras(); //data为B中回传的Intent
                String phone = b.getString("phone");//str即为回传的值
                et_phone.setText(phone);
                }
                break;
            default:
                break;
        }
    }
    @Click
    void btn_back() {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Click
    void show_wheel() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        wheelview.setVisibility(View.VISIBLE);
    }

    @Click
    void btn_close() {
        wheelview.setVisibility(View.GONE);
        tv_address.setText("请选择所在地区");
    }

    @Click
    void btn_ok() {
        tv_address.setText(mCurrentProviceName + "," + mCurrentCityName + ","
                + mCurrentDistrictName);
        wheelview.setVisibility(View.GONE);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mDistrictCode = mDistrictMap.get(mCurrentDistrictName);
        }
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        mProviceCode = mProviceMap.get(mCurrentProviceName);
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
        if (areas == null) {
            areas = new String[]{""};
        }
        mCurrentDistrictName = areas[0];
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);


    }


}
