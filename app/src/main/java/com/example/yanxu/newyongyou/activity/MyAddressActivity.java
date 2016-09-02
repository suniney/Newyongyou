package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.adpter.AddressListAdapter;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Address;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.listener.AddressClickedListener;
import com.example.yanxu.newyongyou.listener.IonSlidingViewClickListener;
import com.example.yanxu.newyongyou.listener.OnRcvScrollListener;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 * 我的收货地址
 */
@EActivity(R.layout.setting_address)
public class MyAddressActivity extends BaseActivity implements HTTPCons {
    @ViewById
    TextView title;
    @ViewById
    TextView add_address;
    @ViewById
    RecyclerView address_list;
    @Extra
    String from;
    private LinearLayoutManager mLayoutManager;
    private AddressListAdapter mAdapter;
    public static final int TYPE_ALL = 1;
    Context mContext;
    ArrayList<Address> address;

    @AfterViews
    void init() {
        mContext = MyAddressActivity.this;
        myInfo();

    }

    private void delete(String id, final int position) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("id", id);
        MyOkHttpUtils.okhttpPost(mContext, deleteAddr_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    mAdapter.notifyDataSetChanged();
                    CommonUtils.ToastShow(mContext, "删除成功");
                } else {
                    String a = String.valueOf(response.getErrors().get(0));
                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void myInfo() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        MyOkHttpUtils.okhttpPost(mContext, myAddr_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                String s1 = String.valueOf(response.getErrors().get(0));
                if (isSucess) {

                    if (!("null".equals(s1))) {
                        address = new Gson().fromJson(s1, new TypeToken<ArrayList<Address>>() {
                        }.getType());
                        mAdapter = new AddressListAdapter(mContext, address, new IonSlidingViewClickListener() {
                            @Override
                            public void onItemClick(View view, int position, String id) {
                            }
                            @Override
                            public void onDeleteBtnCilck(View view, int position, String id) {
                                mAdapter.removeData(position);
                                delete(id, position);
                            }
                        }, new AddressClickedListener() {
                            @Override
                            public void onAddressClick(String addrId, String area, String address, String name, String phone, String isDef) {
                                if ("order".equals(from)) {
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("area", area);
                                    bundle.putString("address", address);
                                    bundle.putString("name", name);
                                    bundle.putString("phone", phone);
                                    bundle.putString("addrId", addrId);
                                    bundle.putString("isDef", isDef);
                                    intent.putExtras(bundle);
                                    setResult(RESULT_ADDRESS, intent);
                                    finish();
                                } else {
                                    ChangeAddressActivity_.intent(mContext).extra("mytitle", "修改收货地址").extra("addrId", addrId).extra("isDef", isDef).extra("phone", phone).extra("name", name).extra("area", area).extra("address", address).start();
                                }
                            }
                        });
                        address_list.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(mContext);
                        address_list.setLayoutManager(mLayoutManager);
                        address_list.setHasFixedSize(true);
                        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                        address_list.setItemAnimator(new DefaultItemAnimator());
                        OnRcvScrollListener listener = new OnRcvScrollListener() {
                            @Override
                            public void onBottom() {
                                // TODO Auto-generated method stub
                                super.onBottom();
                                Toast.makeText(MyAddressActivity.this, "到底部", Toast.LENGTH_SHORT).show();
                            }
                        };
                        address_list.setOnScrollListener(listener);
                        address_list.setAdapter(mAdapter);
                    }


                } else {
                    String a = String.valueOf(response.getErrors().get(0));
                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnActivityResult(RESULT_AddRESS_UPDATE)
    void update() {
        myInfo();
    }
    @Receiver(actions = "com.yinduo.yongyou.addrupdata")
    void updata() {
        myInfo();
    }
    @Click
    void add_address() {
        AddAddressActivity_.intent(this).start();
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
}
