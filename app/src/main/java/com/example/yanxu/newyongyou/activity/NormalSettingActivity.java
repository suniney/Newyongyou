package com.example.yanxu.newyongyou.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.UISwitchButton;
import com.example.yanxu.newyongyou.view.dialog.CustomDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by yanxu 2016/4/1.
 * 关于佣友
 */
@EActivity(R.layout.setting_normal)
public class NormalSettingActivity extends BaseActivity implements HTTPCons {
    @ViewById
    TextView tv_tel;
    @ViewById
    UISwitchButton push_btn;
    Context mContext;
    private boolean push = false;

    @AfterViews
    void init() {
        mContext = NormalSettingActivity.this;
        getInfo();
//        push = SharedPreferencesUtil.getValueByKey(mContext, "push", false);
//        if (push) {
//            push_btn.setChecked(true);
//        } else {
//            push_btn.setChecked(false);
//        }
        push_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    PushAgent mPushAgent = PushAgent.getInstance(mContext);
//                    mPushAgent.enable();
//                    //开启推送并设置注册的回调处理
//                    mPushAgent.enable(new IUmengRegisterCallback() {
//                        @Override
//                        public void onRegistered(final String registrationId) {
//                            Toast.makeText(mContext, "开启了推送", Toast.LENGTH_SHORT).show();
//                            new Handler().post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //onRegistered方法的参数registrationId即是device_token
//                                    android.util.Log.d("device_token", registrationId);
//                                    SharedPreferencesUtil.writeValueByKey(mContext, "push", true);
//                                }
//                            });
//                        }
//                    });
//                } else {
//                    PushAgent mPushAgent = PushAgent.getInstance(mContext);
//                    mPushAgent.disable();
//                    //开启推送并设置注册的回调处理
//                    mPushAgent.disable(new IUmengUnregisterCallback() {
//                        @Override
//                        public void onUnregistered(String s) {
//                            SharedPreferencesUtil.writeValueByKey(mContext, "push", false);
//                            Toast.makeText(mContext, "关闭了推送", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
            }
        });
    }

    private void getInfo() {
        MyOkHttpUtils.okhttpGet(this, get_normalSettings, new CommonCallback() {
                    @Override
                    public void onResponse(Common response, int id) {
                        boolean isSucess = response.isSuccess();
                        if (isSucess) {
                            String s1 = String.valueOf(response.getErrors().get(0));
                            if ("null".equals(s1)) {
                                tv_tel.setVisibility(View.VISIBLE);
                            } else {
                                tv_tel.setText(s1);
                            }

                        } else {
                            Toast.makeText(mContext, String.valueOf(response.getErrors().get(0)), Toast.LENGTH_SHORT).show();
                        }


                    }
                }

        );
    }

    @Click
    void tel_main() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle(tv_tel.getText().toString());
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });
        builder.setNegativeButton("呼叫",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv_tel.getText().toString()));
                        if (ActivityCompat.checkSelfPermission(NormalSettingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        NormalSettingActivity.this.startActivity(intent);
                        dialog.dismiss();
                    }
                });
        builder.create().show();

    }

    @Click
    void iv_back() {
        this.finish();
    }


}
