package com.example.yanxu.newyongyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by yanxu 2016/4/1.
 */
@EActivity(R.layout.splash)
public class SplashActivity extends Activity {

    @ViewById
    View layout_splash;
    private Handler handler;
    private Context context;
    private boolean isFirstIn = false;
    private boolean isFirstLogin = false;
    public static final int GO_HOME = 1000;                 //主页状态
    public static final int GO_GUIDE = 1001;                //引导页状态
    public static final long SPLASH_DELAY_MILLIS = 2000;    //延迟2秒


    @AfterViews
    void init() {



        context = SplashActivity.this;

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GO_HOME:
                        goHome();
                        break;
                    case GO_GUIDE:
                        goGuide();
                        break;
                }
                super.handleMessage(msg);
            }
        };
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(2000);
        layout_splash.startAnimation(aa);
        aa.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                isFirstRun();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });

    }

    /**
     * 判断当前版本与服务器版本是否一致
     *
     * @return true 一致 false 不一致进行下载
     */
    private void isFirstRun() {
        // 取得相应的值，如果没有该值，说明还未写入，用false作为默认值
        isFirstIn = SharedPreferencesUtil.getValueByKey(context, "isFirstIn", false);
        // 判断程序第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (isFirstIn) {
            handler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else {
            handler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        }

    }

    /**
     * 进入首页
     */
    private void goHome() {
        isFirstLogin = SharedPreferencesUtil.getValueByKey(context, "isFirstLogin", false);

        if (isFirstLogin) {
            Intent intent = new Intent(SplashActivity.this, CheckGestureActivity.class);
            startActivity(intent);
            finish();
        } else {
            MainActivity_.intent(this).start();
            finish();
        }

    }

    /**
     * 进入欢迎页
     */
    private void goGuide() {
        GuideActivity_.intent(context).start();
        finish();
    }

}
