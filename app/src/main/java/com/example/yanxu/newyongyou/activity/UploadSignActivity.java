package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 * 上传合同
 */
@EActivity(R.layout.uplaod_sign)
public class UploadSignActivity extends BaseActivity implements HTTPCons {

    @ViewById
    RelativeLayout uplaod_sign;
    @ViewById
    Button btn_add;
    @ViewById
    TextView tv_add;
    @Extra
    String orderId;
    String photo;
    Context mContext;

    @AfterViews
    void init() {
        mContext = UploadSignActivity.this;
//       setTranStatusBar();
    }

    @Click
    void btn_next() {
        if (photo != null) {
            uploadSignPic();
        }else{
            Toast.makeText(mContext, "请上传照片！", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadSignPic() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("orderId", orderId);
        map.put("pic", photo);
        MyOkHttpUtils.okhttpPost(mContext, uploadSignPic_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    CommonUtils.ToastShow(mContext, "提交成功");
                    UploadSignActivity.this.setResult(RESULT_ORDER);
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
            uplaod_sign.setBackground(new BitmapDrawable(bitmap));

        }
    }


}
