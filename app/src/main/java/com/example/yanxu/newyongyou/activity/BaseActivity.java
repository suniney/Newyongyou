package com.example.yanxu.newyongyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yanxu 2016/4/1.
 */
public class BaseActivity extends FragmentActivity {

    public final static int RESULT_PHONE = 0;


    public final static int RESULT_ADDRESS = 1001;
    public final static int RESULT_SIGN_PHOTO = 1002;
    public final static int RESULT_AddRESS_UPDATE = 1003;
    public static final int AVATAR_REQUEST_CODE = 1004;
    public static final int PHOTO_PICKED_WITH_DATA = 1005;
    public static final int GALLERY_AVATAR_REQUEST_CODE = 1006;
    public final static int RESULT_ORDER = 1007;
    public final static int RESULT_SET= 1008;
    public final static int RESULT_WITHDRAW= 1009;
    public final static int RESULT_MSG= 1010;
    public final static int RESULT_CARD= 1011;
    //预约失败
    public static final int STATUS_BOOK_UNSUCCESS = 13;
    //预约中审核
    public static final int STATUS_BOOK_CHECK = 2;
    //预约成功
    public static final int STATUS_BOOK_SUCCESS = 3;
    //重新上传打款凭证
    public static final int STATUS_UPLOAD_MONEY = 4;
    //打款凭证审核
    public static final int STATUS_UPLOAD_CHECK = 5;
    //客户完成交易
    public static final int STATUS_FINISH = 6;
    //重新上传合同
    public static final int STATUS_REUPLOAD_SIGN = 7;
    //合同签署审核
    public static final int STATUS_UPLOAD_SIGN = 8;
    //首次返佣
    public static final int STATUS_BACK_MONEY = 9;
    //回收合同
    public static final int STATUS_RECYCLE_SIGN = 10;
    //订单完成
    public static final int STATUS_OVER = 11;
    //订单关闭
    public static final int STATUS_CLOSE = 12;
//    public SwipeBackLayout swipeBackLayout;
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
//        PushAgent.getInstance(BaseActivity.this).onAppStart();
//        overridePendingTransition(R.anim.activity_ani_enter, 0);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        // 解决输入时键盘挡住内容
//        getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        getWindow().getDecorView().setBackgroundDrawable(null);
//        swipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
//                R.layout.base, null);
//        swipeBackLayout.attachToActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
		overridePendingTransition(0, R.anim.activity_ani_exist);
    }
    protected void startAct(Activity act, Class target) {
        Intent intent = new Intent(act, target);
        startActivity(intent);
    }

    protected String getJsonString(JSONObject obj, String key) {
        return CommonUtils.getJsonString(obj, key);
    }

    protected JSONObject getJsonObject(JSONObject obj, String key) {
        return CommonUtils.getJsonObject(obj, key);
    }

    protected int getJsonInt(JSONObject obj, String key) {

        return CommonUtils.getJsonInt(obj, key);
    }

    protected JSONArray getJsonArray(JSONObject obj, String key) {
        return CommonUtils.getJsonArray(obj, key);
    }

    protected void setTranStatusBar() {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			getWindow().addFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public int dp2px(int dpValue) {
        return (int) (dpValue * getResources().getDisplayMetrics().density);
    }

    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}
