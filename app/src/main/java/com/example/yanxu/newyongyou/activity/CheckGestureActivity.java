package com.example.yanxu.newyongyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;
import com.example.yanxu.newyongyou.utils.VersionUtils;
import com.example.yanxu.newyongyou.widget.GestureContentView;
import com.example.yanxu.newyongyou.widget.GestureDrawline;

import java.io.File;

/**
 * Created by yd on 2016/4/6.
 * 检查手势密码
 */
public class CheckGestureActivity extends Activity implements View.OnClickListener {
    private long mExitTime;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private int count = 5;//解锁操作三次
    private String gestureCode;
    private TextView mHeadTextView;
    private Context context;
    private TextView forgetTextView;
//    private TextView resetTextView;
//    private TextView mobileTextView;
    private ImageView gesturepwd_unlock_face;
    private TextView unlock_version_txt;
    private boolean isFirstIn = false;
    private boolean isFirstLogin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesturepassword_unlock_activity);
        context = CheckGestureActivity.this;
//        setUpListeners();
        isFirstIn = SharedPreferencesUtil.getValueByKey(CheckGestureActivity.this, "isFirstIn", false);
        isFirstLogin = SharedPreferencesUtil.getValueByKey(CheckGestureActivity.this, "isFirstLogin", false);
        if (!isFirstIn) {
            GuideActivity_.intent(context).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
            finish();
        } else {
            if (!isFirstLogin) {
                MainActivity_.intent(context).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
                finish();
            } else if (isFirstLogin) {
                SharedPreferences sp = getSharedPreferences("gesturePsw", Context.MODE_PRIVATE);
                gestureCode = sp.getString("gesturePsw", "0000");
                setUpViews();

            }
        }
    }

    private void setUpViews() {

        SharedPreferences sp = getSharedPreferences("gesturePsw", Context.MODE_PRIVATE);
        gestureCode = sp.getString("gesturePsw", "0000");
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        forgetTextView = (TextView) this.findViewById(R.id.gesturepwd_unlock_forget);
        gesturepwd_unlock_face = (ImageView) this.findViewById(R.id.unlock_gesture_psd_icon);
        mHeadTextView = (TextView) findViewById(R.id.gesturepwd_unlock_text);
        unlock_version_txt = (TextView) findViewById(R.id.unlock_version_txt);
        unlock_version_txt.setText("version:" + VersionUtils.getVersion(CheckGestureActivity.this));
        forgetTextView.setOnClickListener(this);
        File file = new File("/sdcard/avatar.png");
        if (file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile("/sdcard/avatar.png");
            //将图片显示到ImageView中
            gesturepwd_unlock_face.setImageBitmap(bm);
        }
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, true, gestureCode,
                new GestureDrawline.GestureCallBack() {

                    @Override
                    public void onGestureCodeInput(String inputCode) {

                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
                        Toast.makeText(CheckGestureActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
                        MainActivity_.intent(CheckGestureActivity.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                        finish();
                    }

                    @Override
                    public void checkedFail() {
                        count--;
//                        if (count > -1) {
                        if (count > 0) {
                            mGestureContentView.clearDrawlineState(1300L);
                            mHeadTextView.setVisibility(View.VISIBLE);
                            mHeadTextView.setText("密码错误，还有" + count + "次机会");
                            // 左右移动动画
                            Animation shakeAnimation = AnimationUtils.loadAnimation(CheckGestureActivity.this, R.anim.shake);
                            mHeadTextView.startAnimation(shakeAnimation);
                        } else {
                            showExceedTimes();
                        }
                    }
                });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gesturepwd_unlock_forget:
                SharedPreferencesUtil.writeValueByKey(CheckGestureActivity.this, "isFirstLogin", false);
                SharedPreferencesUtil.writeValueByKey(CheckGestureActivity.this, "userId", "");
                SharedPreferencesUtil.writeValueByKey(CheckGestureActivity.this, "mobile", "");
                CommonUtils.sendBroadCast(CheckGestureActivity.this, "com.yinduo.yongyou.loginupdata", null, null);
                LoginActivity_.intent(context).extra("isforgetpwd", true).extra("from", "unlock").start();
                this.finish();
                break;
            default:
                break;
        }
    }

    private void showExceedTimes() {
        Toast.makeText(CheckGestureActivity.this, "您已连续5次输入手势密码错误，请重新登陆", Toast.LENGTH_SHORT).show();
        LoginActivity_.intent(context).extra("isforgetpwd", true).extra("from", "unlock").flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
        finish();
    }

}

