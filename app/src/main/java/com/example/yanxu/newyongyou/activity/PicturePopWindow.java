package com.example.yanxu.newyongyou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.utils.CommonUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;

/**
 * Created by yanxu 2016/4/1.
 * 拍照弹出页
 */
@EActivity(R.layout.bottom_alert_dialog)
public class PicturePopWindow extends BaseActivity {
//    AjaxCallback<JSONObject> uploadHeadimg;
//    AQuery aq;
    public static final String IMAGE_FILE_NAME = "avatar.jpg";
    @ViewById
    RelativeLayout btn_take_photo;
    @ViewById
    RelativeLayout btn_pick_photo;
    @ViewById
    RelativeLayout btn_cancel;
    @ViewById
    RelativeLayout bottom_alert_dialog_nulllayout;
    private static File mCurrentPhotoFile;
    private Bitmap photo;

    @AfterViews
    void init() {
        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        bottom_alert_dialog_nulllayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
//        uploadHeadimg = new AjaxCallback<JSONObject>() {
//
//            @Override
//            public void callback(String url, JSONObject html, AjaxStatus status) {
//                if (status.getCode() == -101) {
//                    Toast.makeText(PicturePopWindow.this, "网络异常,请检查网络", Toast.LENGTH_SHORT);
//                    return;
//                }
//                if (status.getCode() != 200) {
//                    Toast.makeText(aq.getContext(), "网络异常:" + status.getCode(),
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    try {
//                        if (html.getBoolean("isSuccess")) {
//                            Toast.makeText(aq.getContext(), "上传成功",
//                                    Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(aq.getContext(),
//                                    html.getString("errors"), Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        };

    }

    @Click
    void btn_take_photo() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 判断存储卡是否可以用，可用进行存储
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getExternalFilesDir("photo"), IMAGE_FILE_NAME)));
            startActivityForResult(intentFromCapture,
                    AVATAR_REQUEST_CODE);
        }
        else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    @Click
    void btn_pick_photo() {
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*"); // 设置文件类型
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery,
                GALLERY_AVATAR_REQUEST_CODE);
    }

    @Click
    void btn_cancel() {
        finish();
    }

    @OnActivityResult(GALLERY_AVATAR_REQUEST_CODE)
    void onResultGallery(int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (data != null) {
            Intent intent = CommonUtils.startPhotoZoom(data.getData());
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        }
    }

    @OnActivityResult(AVATAR_REQUEST_CODE)
    void onResultTakeAvatar(int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        File tempFile = new File(getExternalFilesDir("photo"), IMAGE_FILE_NAME);
        Intent intent = CommonUtils.startPhotoZoom(Uri.fromFile(tempFile));
        startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
    }

    @OnActivityResult(PHOTO_PICKED_WITH_DATA)
    void onResultGetAvatar(int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (data != null) {
            setImageToView(data);
        }
    }

    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            String temp = "";
            try {
                temp = CommonUtils.encodeBase64File(CommonUtils
                        .saveBitmap(photo));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //temp 为当前头像
            Intent intent = new Intent();
            intent.setAction("photo");
            intent.putExtra("temp", temp);
            intent.putExtra("bitmap", photo);
            this.setResult(RESULT_SIGN_PHOTO, intent);
            this.finish();

        }
    }
}
