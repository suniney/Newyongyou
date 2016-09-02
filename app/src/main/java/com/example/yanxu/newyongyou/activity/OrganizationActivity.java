package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 * 身份认证
 */
@EActivity(R.layout.setting_idcard)
public class OrganizationActivity extends BaseActivity implements HTTPCons {

    @ViewById
    RelativeLayout show_id;
    @ViewById
    Button btn_add;
    @ViewById
    TextView tv_add;
    @ViewById
    EditText organization_name;
    @ViewById
    EditText organization_phone;
    @ViewById
    EditText organization_tradetype;

    Context mContext;
    String photo;

    @AfterViews
    void init() {
        mContext = OrganizationActivity.this;
//       setTranStatusBar();
    }

    @Click
    void btn_next() {
        String mobile = organization_phone.getText().toString().trim();
        String realName = organization_name.getText().toString().trim();
        String tradetype = organization_tradetype.getText().toString().trim();
        if (TextUtils.isEmpty(realName)) {
            Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CommonUtils.isMobileNO(mobile)) {
            Toast.makeText(mContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(tradetype)) {
            Toast.makeText(mContext, "请输入行业", Toast.LENGTH_SHORT).show();
        } else if (photo == null) {
            Toast.makeText(mContext, "请上传照片", Toast.LENGTH_SHORT).show();
        } else {
            saveOrganization(mobile, realName, tradetype);
        }

    }

    private void saveOrganization(String mobile, String realName, String tradetype) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("mobile", mobile);
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        map.put("realName", realName);
        map.put("tradetype", tradetype);
        map.put("photo", photo);
        MyOkHttpUtils.okhttpPost(mContext, add_saveOrganization_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    CommonUtils.ToastShow(mContext, "提交成功");
                    OrganizationActivity.this.setResult(RESULT_SET);
                    finish();
                } else {
                    String a = String.valueOf(response.getErrors().get(0));
                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void btn_add() {
        PicturePopWindow_.intent(this).startForResult(RESULT_SIGN_PHOTO);
    }

    @OnActivityResult(RESULT_SIGN_PHOTO)
    void onResultSignPhoto(int resultCode, Intent intent) {
        if (resultCode == RESULT_SIGN_PHOTO) {
            Bitmap bitmap;
            bitmap = intent.getParcelableExtra("bitmap");
            photo = intent.getStringExtra("temp");
            show_id.setBackground(new BitmapDrawable(bitmap));

        }
    }

}
