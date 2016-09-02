package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 * 忘记密码
 */
@EActivity(R.layout.forget_pwd)
public class ForgetPwdActivity extends BaseActivity implements HTTPCons {
    @ViewById
    Button send_code;
    @ViewById
    EditText et_mobile;
    @ViewById
    EditText et_code;
    @ViewById
    EditText et_pwd;
    Context mContext;
    CountDownTimer timer;
    @AfterViews
    void init() {
        mContext = ForgetPwdActivity.this;
//       setTranStatusBar();
    }

    @Click
    void btn_sure() {
        String mobile = et_mobile.getText().toString().trim();
        String passwd = et_pwd.getText().toString().trim();
        String verifyCode = et_code.getText().toString().trim();
        if (!CommonUtils.isCorrectUserPwd(passwd)) {
            Toast.makeText(mContext, "请输入包含英文字母的6-12位密码", Toast.LENGTH_SHORT).show();
        } else if (!CommonUtils.isMobileNO(mobile)) {
            Toast.makeText(mContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(verifyCode)) {
            Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
        } else {
            resetPwd(mobile, passwd, verifyCode);
        }
    }

    @Click
    void send_code() {
        String mobile = et_mobile.getText().toString().trim();
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
                    send_code.setClickable(false);

                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub
                    // get back to normal
                    send_code.setClickable(true);
                    send_code.setText(R.string.code_finish);
                }
            };
            timer.start();

        }
    }

    private void resetPwd(final String mobile, String passwd, String verifyCode) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("mobile", mobile);
        map.put("verifyCode", verifyCode);
        map.put("passwd", passwd);
        MyOkHttpUtils.okhttpPost(this, resetPwd_action, map, new CommonCallback() {

            @Override
            public void onResponse(Common response, int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    String str = String.valueOf(response.getErrors().get(0));
                    if (str.equals("修改成功")) {
                        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(mContext, String.valueOf(response.getErrors().get(0)), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, String.valueOf(response.getErrors().get(0)), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //codeType 1是注册验证码。3是忘记密码
    //flag :register_code 是注册  forget_code是忘记密码
    private void sendVerifyCode(final String mobile) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("mobile", mobile);
        map.put("flag", "forget_code");
        map.put("codeType", "3");
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
                }
            }
        });
    }

    @Click
    void btn_back() {
        this.finish();
    }
}
