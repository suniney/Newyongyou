package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.UpdateService;
import com.example.yanxu.newyongyou.fragment.HomeFragment;
import com.example.yanxu.newyongyou.fragment.HomeFragment_;
import com.example.yanxu.newyongyou.fragment.OrderFragment;
import com.example.yanxu.newyongyou.fragment.OrderFragment_;
import com.example.yanxu.newyongyou.fragment.SettingFragment;
import com.example.yanxu.newyongyou.fragment.SettingFragment_;
import com.example.yanxu.newyongyou.fragment.WealthFragment;
import com.example.yanxu.newyongyou.fragment.WealthFragment_;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;
import com.example.yanxu.newyongyou.utils.VersionUtils;
import com.example.yanxu.newyongyou.view.dialog.CustomDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

/**
 * Created by yanxu 2016/4/1.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements HTTPCons {
    @ViewById
    LinearLayout first_ll;
    @ViewById
    ImageView first_image;
    @ViewById
    TextView first_text;
    @ViewById
    LinearLayout second_ll;
    @ViewById
    ImageView second_image;
    @ViewById
    TextView second_text;
    @ViewById
    LinearLayout third_ll;
    @ViewById
    ImageView third_image;
    @ViewById
    TextView third_text;
    @ViewById
    LinearLayout forth_ll;
    @ViewById
    ImageView forth_image;
    @ViewById
    TextView forth_text;
    private boolean isFirstLogin = false;
    private Context context;
    private final String TAG = "test";
    private FragmentManager fragmentManager;
    private HomeFragment firstFragment;
    private OrderFragment secondFragment;
    private WealthFragment thirdFragment;
    private SettingFragment forthFragment;
    private long mExitTime;
    private int currentItem = 0;
    // APP name
    public static final String appName = "yongyou";
    //APP address
    public static final String downUrl = "http://app.auto11.com/app/Auto11_v1.1.7.apk";
    //APP version
    public static final String version = "1.0";
    @AfterViews
    void init() {
        context = MainActivity.this;
        // 初始化布局元素
        fragmentManager = getSupportFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);
        setBar(0);
        if (!version.equals(VersionUtils.getVersion(context))) {
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setTitle("下载更新");
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //设置你的操作事项
                }
            });
            builder.setNegativeButton("下载",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*****update service*******/
                            Intent intent = new Intent(MainActivity.this, UpdateService.class);
                            intent.putExtra("Key_App_Name", appName);
                            intent.putExtra("Key_Down_Url", downUrl);
                            startService(intent);
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
//        savedInstanceState
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    private void setBar(int mpage) {
        switch (mpage) {
            case 0:
                first_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
                first_image.setImageResource(R.drawable.main_home_img_check);
                break;
            case 1:
                second_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
                second_image.setImageResource(R.drawable.main_order_img_check);
                break;
            case 2:
                third_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
                third_image.setImageResource(R.drawable.main_wealth_img_check);
                break;
            case 3:
                forth_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
                forth_image.setImageResource(R.drawable.main_user_img_check);
                break;

        }
    }

    @Click
    void first_ll() {
        if (currentItem != 0) {
            // 当点击了首页tab时，选中第1个tab
            setTabSelection(0);
            currentItem = 0;
            first_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
            first_image.setImageResource(R.drawable.main_home_img_check);
        }
    }

    @Click
    void second_ll() {
        isFirstLogin = SharedPreferencesUtil.getValueByKey(context, "isFirstLogin", false);
        if (isFirstLogin) {
            if (currentItem != 1) {
                // 当点击了银多理财tab时，选中第2个tab
                setTabSelection(1);
                currentItem = 1;
                second_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
                second_image.setImageResource(R.drawable.main_order_img_check);
            }
        } else {
            LoginActivity_.intent(this).extra("from", "1").start();
        }

    }

    @Receiver(actions = "updata")
    protected void updata(Intent intent) {
        String page = intent.getExtras().getString("from");
        int mpage = Integer.valueOf(page);
        currentItem = mpage;
        if (mpage == 1) {
            setTabSelection(mpage);
            second_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
            second_image.setImageResource(R.drawable.main_order_img_check);
        } else if (mpage == 2) {
            setTabSelection(mpage);
            third_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
            third_image.setImageResource(R.drawable.main_wealth_img_check);
        } else if (mpage == 0) {
            setTabSelection(mpage);
            first_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
            first_image.setImageResource(R.drawable.main_home_img_check);
        } else if (mpage == 3) {
            setTabSelection(0);
            first_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
            first_image.setImageResource(R.drawable.main_home_img_check);
        } else if (mpage == 4) {
            setTabSelection(0);
            first_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
            first_image.setImageResource(R.drawable.main_home_img_check);
            MessageActivity_.intent(MainActivity.this).start();
        } else if (mpage == 5) {
            setTabSelection(3);
            forth_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
            forth_image.setImageResource(R.drawable.main_user_img_check);
        }

    }

    @OnActivityResult(BaseActivity.RESULT_MSG)
    void onResultMsg(int requestCode, int resultCode, Intent intent) {
        if (resultCode == BaseActivity.RESULT_MSG) {
            firstFragment.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @OnActivityResult(BaseActivity.RESULT_ORDER)
    void onResultSign(int requestCode, int resultCode, Intent intent) {
        if (resultCode == BaseActivity.RESULT_ORDER) {
            secondFragment.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @OnActivityResult(BaseActivity.RESULT_SET)
    void onResultSet(int requestCode, int resultCode, Intent intent) {
        if (resultCode == BaseActivity.RESULT_SET) {
            forthFragment.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @OnActivityResult(BaseActivity.RESULT_SIGN_PHOTO)
    void onResultPhoto(int requestCode, int resultCode, Intent intent) {
        if (resultCode == BaseActivity.RESULT_SIGN_PHOTO) {
            firstFragment.onActivityResult(requestCode, resultCode, intent);
            forthFragment.onActivityResult(requestCode, resultCode, intent);

        }
    }

    @Click
    void third_ll() {
        isFirstLogin = SharedPreferencesUtil.getValueByKey(context, "isFirstLogin", false);
        if (isFirstLogin) {
            if (currentItem != 2) {
                //当点击了发现tab时，选中第3个tab
                setTabSelection(2);
                currentItem = 2;
                third_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
                third_image.setImageResource(R.drawable.main_wealth_img_check);
            }
        } else {
            LoginActivity_.intent(this).extra("from", "2").start();
        }
    }

    @Click
    void forth_ll() {
        if (currentItem != 3) {
            // 当点击了动态tab时，选中第4个tab
            setTabSelection(3);
            currentItem = 3;
            forth_text.setTextColor(getResources().getColor(R.color.text_deep_yellow));
            forth_image.setImageResource(R.drawable.main_user_img_check);
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
//        if (firstFragment != null) {
//            transaction.hide(firstFragment);
//        }
//        if (secondFragment != null) {
//            transaction.hide(secondFragment);
//        }
//        if (thirdFragment != null) {
//            transaction.hide(thirdFragment);
//        }
//        if (forthFragment != null) {
//            transaction.hide(forthFragment);
//        }

        switch (index) {
            case 0:


                firstFragment = new HomeFragment_();
                transaction.replace(R.id.content, firstFragment);
//                if (firstFragment == null) {
//                    firstFragment = new HomeFragment_();
//                    transaction.add(R.id.content, firstFragment);
//
//                } else {
//                    // 如果不为空，则直接将它显示出来
//                    transaction.show(firstFragment);
////                    transaction.attach(firstFragment);
//                }
                break;
            case 1:
                secondFragment = new OrderFragment_();
                transaction.replace(R.id.content, secondFragment);
//                if (secondFragment == null) {
//                    // 如果为空，则创建一个并添加到界面上
//                    secondFragment = new OrderFragment_();
//                    transaction.add(R.id.content, secondFragment);
//
//                } else {
//                    // 如果不为空，则直接将它显示出来
//                    transaction.show(secondFragment);
////                    transaction.attach(secondFragment);
//                }
                break;

            case 2:
                thirdFragment = new WealthFragment_();
                transaction.replace(R.id.content, thirdFragment);
//                if (thirdFragment == null) {
//                    // 如果为空，则创建一个并添加到界面上
//                    thirdFragment = new WealthFragment_();
//                    transaction.add(R.id.content, thirdFragment);
//                } else {
//                    // 如果不为空，则直接将它显示出来
//                    transaction.show(thirdFragment);
////                    transaction.attach(thirdFragment);
//                }
                break;

            case 3:
                forthFragment = new SettingFragment_();
                transaction.replace(R.id.content, forthFragment);
//                if (forthFragment == null) {
//                    forthFragment = new SettingFragment_();
//                    transaction.add(R.id.content, forthFragment);
//                } else {
//                    transaction.show(forthFragment);
////                    transaction.attach(forthFragment);
//                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        first_text.setTextColor(getResources().getColor(R.color.white));
        first_image.setImageResource(R.drawable.main_home_img);
        second_text.setTextColor(getResources().getColor(R.color.white));
        second_image.setImageResource(R.drawable.main_order_img);
        third_text.setTextColor(getResources().getColor(R.color.white));
        third_image.setImageResource(R.drawable.main_wealth_img);
        forth_text.setTextColor(getResources().getColor(R.color.white));
        forth_image.setImageResource(R.drawable.main_user_img);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {

        if (firstFragment != null) {
            transaction.hide(firstFragment);
//            transaction.detach(firstFragment);
        }
        if (secondFragment != null) {
            transaction.hide(secondFragment);
//            transaction.detach(secondFragment);
        }
        if (thirdFragment != null) {
            transaction.hide(thirdFragment);
//            transaction.detach(thirdFragment);
        }
        if (forthFragment != null) {
            transaction.hide(forthFragment);
//            transaction.detach(forthFragment);
        }
    }


    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
