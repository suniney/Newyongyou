package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedHashMap;
import java.util.Map;

@EActivity(R.layout.login)
/**
 * Created by yanxu 2016/4/1.
 */
public class LoginActivity extends BaseActivity implements HTTPCons {
    @ViewById
    EditText regigst_phone;
    @ViewById
    EditText et_code;
    @ViewById
    EditText regigst_pwd;
    @ViewById
    EditText et_login_pwd;
    @ViewById
    EditText et_login_phone;
    @Extra
    String from;
    @Extra
    String proId;
    @Extra
    String userName;
    @Extra
    Boolean isforgetpwd;
    @ViewById
    Button btn_login;
    @ViewById
    Button btn_register;
    @ViewById
    LinearLayout ll_regist;
    @ViewById
    LinearLayout ll_login;
    @ViewById
    TextView login;
    @ViewById
    TextView regist;
    @ViewById
    CheckBox check;
    @ViewById
    Button send_code;
    @ViewById
    EditText referrer_num;
    Context mContext;
    //    private Boolean isforgetpwd = false;
    private long mExitTime;
    CountDownTimer timer;

    @AfterViews
    void init() {
        mContext = LoginActivity.this;
        if (!"".equals(SharedPreferencesUtil.getValueByKey(mContext, "username", ""))) {
            et_login_phone.setText(SharedPreferencesUtil.getValueByKey(mContext, "username", ""));
        }


    }


    @Click
    void login() {
        ll_regist.setVisibility(View.GONE);
        ll_login.setVisibility(View.VISIBLE);
        login.setTextColor(getResources().getColor(R.color.text_money));
        regist.setTextColor(getResources().getColor(R.color.white));
    }

    @Click
    void send_code() {
//        if (!haveNetworkConnection()) {
//            Toast.makeText(mContext, "手机无网络", Toast.LENGTH_SHORT).show();
//        } else {
        String mobile = regigst_phone.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
        } else if (!CommonUtils.isMobileNO(mobile)) {
            Toast.makeText(mContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
        } else {
            sendVerifyCode(mobile);
//            Toast.makeText(mContext, "已发送", Toast.LENGTH_SHORT).show();
            timer = new CountDownTimer(period, step) {
                @Override
                public void onTick(long arg0) {
                    // TODO Auto-generated method stub
                    send_code.setText(getString(
                            R.string.code,
                            arg0 / 1000));
                    send_code.setTextColor(getResources().getColor(R.color.text_code));
                    send_code.setBackground(getResources().getDrawable(R.drawable.regist_code_bg_1));
                    send_code.setClickable(false);
                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub
                    // get back to normal
                    send_code.setClickable(true);
                    send_code.setTextColor(getResources().getColor(R.color.white));
                    send_code.setBackground(getResources().getDrawable(R.drawable.regist_code_bg));
                    send_code.setText(R.string.code_finish);
                }
            };
            timer.start();
        }
//        }


    }

    @Click
    void forget_pwd() {
        ForgetPwdActivity_.intent(this).start();
    }

    @Click
    void regist() {
        ll_login.setVisibility(View.GONE);
        ll_regist.setVisibility(View.VISIBLE);
        regist.setTextColor(getResources().getColor(R.color.text_money));
        login.setTextColor(getResources().getColor(R.color.white));
    }

    @Click
    void btn_register() {
        if (!haveNetworkConnection()) {
            Toast.makeText(mContext, "手机无网络", Toast.LENGTH_SHORT).show();
        } else {
            String mobile = regigst_phone.getText().toString().trim();
            String passwd = regigst_pwd.getText().toString().trim();
            String referMobile = referrer_num.getText().toString().trim();
            String verifyCode = et_code.getText().toString().trim();
            if (TextUtils.isEmpty(mobile)) {
                Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
            } else if (!CommonUtils.isMobileNO(mobile)) {
                Toast.makeText(mContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(verifyCode)) {
                Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
            } else if (!CommonUtils.isCorrectUserPwd(passwd)) {
                Toast.makeText(mContext, "请输入包含英文字母的6-12位密码", Toast.LENGTH_SHORT).show();
            } else if (!check.isChecked()) {
                Toast.makeText(mContext, "需同意《佣友的服务协议》", Toast.LENGTH_SHORT).show();
            } else {
                toRegist(mobile, passwd, referMobile, verifyCode);
                btn_register.setClickable(false);
            }
        }
    }

    @Click
    void btn_login() {
        if (!haveNetworkConnection()) {
            Toast.makeText(mContext, "手机无网络", Toast.LENGTH_SHORT).show();
        } else {
            String mobile = et_login_phone.getText().toString().trim();
            String passwd = et_login_pwd.getText().toString().trim();
            if (TextUtils.isEmpty(mobile)) {
                Toast.makeText(mContext, "昵称不能为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(passwd)) {
                Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
            } else if (!CommonUtils.isCorrectUserPwd(passwd)) {
                Toast.makeText(mContext, "请输入6-20位数字或字母", Toast.LENGTH_SHORT).show();
            } else {
                toLogin(mobile, passwd);
                btn_login.setClickable(false);
            }
        }
    }

    private void toGesture(String userId, String mobile) {
        boolean isFirstLogin = SharedPreferencesUtil.getValueByKey(mContext, "isFirstLogin", false);
        if (isFirstLogin) {
            if (isforgetpwd) {
                Intent intent = new Intent(LoginActivity.this, CreateGestureActivity.class);
                intent.putExtra("from", from);
                intent.putExtra("proId", proId);
                intent.putExtra("userId", userId);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(LoginActivity.this, CheckGestureActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(LoginActivity.this, CreateGestureActivity.class);
            intent.putExtra("from", from);
            intent.putExtra("proId", proId);
            intent.putExtra("userId", userId);
            intent.putExtra("mobile", mobile);
            startActivity(intent);
            finish();
        }
    }

    //codeType 1是注册验证码。3是忘记密码
    //flag :register_code 是注册  forget_code是忘记密码
    private void sendVerifyCode(final String mobile) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("mobile", mobile);
        map.put("flag", "register_code");
        map.put("codeType", "1");
        MyOkHttpUtils.okhttpPost(this, sendVerifyCode_action, map, new CommonCallback() {

            @Override
            public void onResponse(Common response, int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
//                    if ("error".equals(String.valueOf(response.getErrors().get(0)))) {
//                        Toast.makeText(mContext, "手机号已存在", Toast.LENGTH_LONG).show();
//                    } else {
                    Toast.makeText(mContext, String.valueOf(response.getErrors().get(0)), Toast.LENGTH_LONG).show();
//                    }
                } else {
                    Toast.makeText(mContext, String.valueOf(response.getErrors().get(0)), Toast.LENGTH_SHORT).show();
                    timer.cancel();
                    send_code.setClickable(true);
                    send_code.setText(R.string.code_finish);
                    send_code.setTextColor(getResources().getColor(R.color.white));
                    send_code.setBackground(getResources().getDrawable(R.drawable.regist_code_bg));
                }
            }
        });
    }

    private void toRegist(final String mobile, String passwd, String referMobile, String verifyCode) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("mobile", mobile);
        map.put("passwd", passwd);
        if (!"".equals(referMobile)) {
            map.put("referMobile", referMobile);
        }
        map.put("verifyCode", verifyCode);
        MyOkHttpUtils.okhttpPost(this, post_regist_action, map, new CommonCallback() {

            @Override
            public void onResponse(Common response, int id) {
                btn_register.setClickable(false);
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                    String userId = String.valueOf(response.getErrors().get(0));
//                    SharedPreferencesUtil.writeValueByKey(mContext, "userId", userId);
                    SharedPreferencesUtil.writeValueByKey(mContext, "mobile", mobile);
                    toGesture(userId, mobile);
                } else {
                    Toast.makeText(mContext, String.valueOf(response.getErrors().get(0)), Toast.LENGTH_SHORT).show();
                    btn_register.setClickable(true);
                }
            }
        });
    }

    private void toLogin(final String mobile, String passwd) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userName", mobile);
        map.put("passwd", passwd);

        MyOkHttpUtils.okhttpPost(this, post_login_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response, int id) {
                btn_login.setClickable(true);
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
                    String userId = String.valueOf(response.getErrors().get(0));
                    toGesture(userId, mobile);
                } else {
                    if (response.getErrors() == null) {
                        Toast.makeText(mContext, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, String.valueOf(response.getErrors().get(0)), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Click
    void ll_back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        MainActivity_.intent(mContext).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
        this.finish();
    }

}
