package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.callBack.ImageCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.entity.UserCenter;
import com.example.yanxu.newyongyou.utils.CircleImageView;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;
import com.example.yanxu.newyongyou.view.dialog.CustomDialog;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 * 用户中心
 */
@EActivity(R.layout.setting_usercenter)
public class UserCenterActivity extends BaseActivity implements HTTPCons {
    @ViewById
    CircleImageView iv_headimg;
    @ViewById
    Button btn_exit;
    @ViewById
    TextView usercenter_isIdentity;
    @ViewById
    TextView usercenter_isBank;
    @ViewById
    TextView usercenter_isPostAddr;
    @ViewById
    TextView usercenter_name;
    @ViewById
    TextView usercenter_mobile;
    @ViewById
    RelativeLayout address;
    @ViewById
    RelativeLayout idcard;
    @ViewById
    RelativeLayout bankcard;
    Context mContext;
    UserCenter userCenter;
    @Extra
    String name;

    @AfterViews
    void init() {
        mContext = UserCenterActivity.this;
        myInfo();
    }


    private void myInfo() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        MyOkHttpUtils.okhttpPost(mContext, userCenter_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    userCenter = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), UserCenter.class);
                    if (userCenter.getIsIdentity() != null) {
                        if ("0".equals(userCenter.getIsIdentity().toString())) {
                            usercenter_isIdentity.setText("已认证");
                        } else {
                            usercenter_isIdentity.setText("未认证");
                        }
                    }
                    if (userCenter.getIsBank() != null) {
                        if ("0".equals(userCenter.getIsBank().toString())) {
                            usercenter_isBank.setText("已添加");
                        } else {
                            usercenter_isBank.setText("未添加");
                        }
                    }
                    if (userCenter.getIsPostAddr() != null) {
                        if ("0".equals(userCenter.getIsPostAddr().toString())) {
                            usercenter_isPostAddr.setText("已填写");
                        } else {
                            usercenter_isPostAddr.setText("未填写");
                        }
                    }
                    usercenter_name.setText(userCenter.getUserName());
                    if (userCenter.getHeadUrl() != null) {
                        MyOkHttpUtils.getImage(userCenter.getHeadUrl(), new ImageCallback() {
                            @Override
                            public void onResponse(Bitmap response,int id) {
                                iv_headimg.setImageBitmap(response);
                            }
                        });
                    }
                    if (userCenter.getMobile() != null) {
                        String a = userCenter.getMobile().substring(0, 3);
                        String b = userCenter.getMobile().substring(3, 7);
                        String c = userCenter.getMobile().substring(7, 11);
                        usercenter_mobile.setText(a + "-" + b + "-" + c);
                    }
                } else {
//                    String a = String.valueOf(response.getErrors().get(0));
//                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Click
    void btn_exit() {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setTitle("确认退出登录么？");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        CommonUtils.sendBroadCast(mContext, "exit", null, null);
                        SharedPreferencesUtil.writeValueByKey(mContext, "isFirstLogin", false);
                        SharedPreferencesUtil.writeValueByKey(mContext, "userId", "");
                        SharedPreferencesUtil.writeValueByKey(mContext, "mobile", "");

                        File file = new File("/sdcard/avatar.png");
                        if (file.exists()) {
                            file.delete();
                        }
                        CommonUtils.sendBroadCast(mContext, "com.yinduo.yongyou.loginupdata", "", null);
                        dialog.dismiss();
                        Toast.makeText(mContext, "退出成功", Toast.LENGTH_SHORT).show();
                        UserCenterActivity.this.finish();
                    }
                });
        builder.create().show();


    }


    @Click
    void iv_back() {
        this.finish();
    }

    @Click
    void address() {
        if (!haveNetworkConnection()) {
            address.setClickable(false);
        } else {
            address.setClickable(true);
            MyAddressActivity_.intent(this).extra("type", userCenter.getIsPostAddr().toString()).startForResult(RESULT_SET);
        }

    }

    @Click
    void changepwd() {
        PwdActivity_.intent(this).extra("userName", userCenter.getUserName()).start();
    }

    @Click
    void idcard() {
        if (!haveNetworkConnection()) {
            idcard.setClickable(false);
        } else {
            idcard.setClickable(true);
            if ("1".equals(userCenter.getIsIdentity().toString())) {
                OrganizationActivity_.intent(this).extra("type", userCenter.getIsIdentity().toString()).startForResult(RESULT_SET);
            }
        }
    }

    @Click
    void bankcard() {
        if (!haveNetworkConnection()) {
            bankcard.setClickable(false);
        } else {
            bankcard.setClickable(true);
            if(userCenter.getIsBank()==null){
                AddCardActivity_.intent(this).extra("type", "1").startForResult(RESULT_SET);
            }else{
                AddCardActivity_.intent(this).extra("type", userCenter.getIsBank().toString()).startForResult(RESULT_SET);
            }
        }

    }

    @OnActivityResult(RESULT_SET)
    void updata() {
        myInfo();
    }

}
