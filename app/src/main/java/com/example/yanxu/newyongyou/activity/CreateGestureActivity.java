package com.example.yanxu.newyongyou.activity;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
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
import com.example.yanxu.newyongyou.widget.GestureContentView;
import com.example.yanxu.newyongyou.widget.GestureDrawline;
import com.example.yanxu.newyongyou.widget.LockIndicator;


public class CreateGestureActivity extends Activity implements View.OnClickListener{
    Context context;
    TextView mTextTip,text_reset;
    ImageView gesturepwd_unlock_face;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    private LockIndicator mLockIndicator;
    String from;
    String proId;
    String userId;
    String mobile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesturepassword_create_activity);
        context = CreateGestureActivity.this;
        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        text_reset = (TextView) findViewById(R.id.text_reset);
        setUpViews();
        from = getIntent().getStringExtra("from");
        text_reset.setOnClickListener(this);

    }

    private void setUpViews() {
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, false, "", new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
                if (!isInputPassValidate(inputCode)) {
                    mTextTip.setText(Html.fromHtml("<font color='#ffa177'>最少链接4个点, 请重新输入</font>"));
                    mGestureContentView.clearDrawlineState(0L);
                    return;
                }
                if (mIsFirstInput) {
                    mFirstPassword = inputCode;
                    updateCodeList(inputCode);
                    mGestureContentView.clearDrawlineState(0L);
                    mTextTip.setTextColor(getResources().getColor(R.color.black_text));
                    mTextTip.setText("请再次绘制手势密码");
                } else {
                    if (inputCode.equals(mFirstPassword)) {
                        userId = getIntent().getStringExtra("userId");
                        mobile = getIntent().getStringExtra("mobile");
                        SharedPreferencesUtil.writeValueByKey(CreateGestureActivity.this, "isFirstLogin", true);
                        SharedPreferencesUtil.writeValueByKey(CreateGestureActivity.this, "userId", userId);
                        SharedPreferencesUtil.writeValueByKey(CreateGestureActivity.this, "username", mobile);
                        SharedPreferences sp = getSharedPreferences("gesturePsw", Context.MODE_PRIVATE);
                        sp.edit().putString("gesturePsw", inputCode).commit();
                        mGestureContentView.clearDrawlineState(0L);
                        if ("setGesture".equals(from)) {
                            Toast.makeText(CreateGestureActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                        } else if ("1".equals(from)) {
                            //未登录点击订单
                            CommonUtils.sendBroadCast(CreateGestureActivity.this, "updata", "from", from);
                            CommonUtils.sendBroadCast(CreateGestureActivity.this, "com.yinduo.yongyou.loginupdata", null, null);
                        } else if ("2".equals(from)) {
                            //未登录点击财富
                            CommonUtils.sendBroadCast(CreateGestureActivity.this, "updata", "from", from);
                            CommonUtils.sendBroadCast(CreateGestureActivity.this, "com.yinduo.yongyou.loginupdata", null, null);
                        } else if ("3".equals(from)) {
                            //未登录点击预约
                            proId = getIntent().getStringExtra("proId");
                            CommonUtils.sendBroadCast(CreateGestureActivity.this, "updata", "from", from);
                            CommonUtils.sendBroadCast(CreateGestureActivity.this, "com.yinduo.yongyou.loginupdata", null, null);
                            OrderActivity_.intent(CreateGestureActivity.this).extra("proId", proId).start();
                        } else if ("4".equals(from)) {
                            //未登录点击消息
                            CommonUtils.sendBroadCast(CreateGestureActivity.this, "updata", "from", from);
                            CommonUtils.sendBroadCast(CreateGestureActivity.this, "com.yinduo.yongyou.loginupdata", null, null);
                        } else if ("5".equals(from)) {
                            //未登录点击未登录
                            CommonUtils.sendBroadCast(CreateGestureActivity.this, "updata", "from", from);
                            CommonUtils.sendBroadCast(CreateGestureActivity.this, "com.yinduo.yongyou.loginupdata", null, null);
                        } else {
                            MainActivity_.intent(CreateGestureActivity.this).start();
                            Toast.makeText(CreateGestureActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    } else {
                        mTextTip.setText(Html.fromHtml("<font color='#ffa177'>与上一次绘制不一致，请重新绘制</font>"));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(CreateGestureActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                        // 保持绘制的线，1.5秒后清除
                        mGestureContentView.clearDrawlineState(1300L);
                    }
                }
                mIsFirstInput = false;
            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {

            }
        });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
        updateCodeList("");


    }

    private void updateCodeList(String inputCode) {
        // 更新选择的图案
        mLockIndicator.setPath(inputCode);
    }

    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_reset:
                mIsFirstInput = true;
                updateCodeList("");
                mTextTip.setText("绘制解锁图片");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        SharedPreferencesUtil.writeValueByKey(CreateGestureActivity.this, "userId", "");
        SharedPreferencesUtil.writeValueByKey(CreateGestureActivity.this, "mobile", "");
        this.finish();
    }
}
