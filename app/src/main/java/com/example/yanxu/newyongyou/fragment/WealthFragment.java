package com.example.yanxu.newyongyou.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.activity.MainActivity;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MsgDigestUtil;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by yanxu 2016/4/1.
 */
@EFragment(R.layout.fg_wealth)
public class WealthFragment extends BaseFragment implements HTTPCons {
    @ViewById
    WebView webview;
    String userName;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String APP_CACHE_DIRNAME = "/webcache"; // web缓存目录
    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }
    @AfterViews
    void init() {
//        clearCacheFolder(mActivity.getCacheDir(), System.currentTimeMillis());//删除此时之前的缓存.
        myInfo();
        WebSettings webSettings = webview.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(false);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                Log.i(TAG, "onLoadResource url=" + url);
                super.onLoadResource(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "intercept url=" + url);
                view.loadUrl(url);
                return true;
            }
            // 页面开始时调用
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "onPageStarted");
                super.onPageStarted(view, url, favicon);
            }
            // 页面加载完成调用
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(mActivity, "同步失败，请稍候再试", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void myInfo() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mActivity, "userId", ""));
        MyOkHttpUtils.okhttpPost(mActivity, myAsset_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    userName = String.valueOf(response.getErrors().get(0));
                    String username = "username=" + Base64.encodeToString(userName.getBytes(), Base64.NO_WRAP | Base64.URL_SAFE);
                    String str = username + "&key=3e9bb86c6980c3b79e5b936ce10b9b96";
                    String key = MsgDigestUtil.sha256Hex(str);
                    String url = webhost + "/index.php/home/Index/achievement?" + username + "&key=" + key;
                    System.out.println(url);
                    if (haveNetworkConnection()){
                        webview.loadUrl(url);
                    }else {
                        webview.loadData("<html><body><h1> NO NETWORK!</h1></body></html>", "text/html", "UTF-8");
                    }

                }
            }
        });
    }


    @Receiver(actions = "com.yinduo.yongyou.loginupdata")
    void updata1() {
        String username = "username=" + Base64.encodeToString(userName.getBytes(), Base64.NO_WRAP | Base64.URL_SAFE);
        String str = username + "&key=3e9bb86c6980c3b79e5b936ce10b9b96";
        String key = MsgDigestUtil.sha256Hex(str);
        String url = webhost + "/index.php/home/Index/achievement?" + username + "&key=" + key;
        if (haveNetworkConnection()){
            webview.loadUrl(url);
        }else {
            webview.loadData("<html><body><h1> NO NETWORK!</h1></body></html>", "text/html", "UTF-8");
        }
    }

    @Receiver(actions = "com.yinduo.yongyou.nameupdata")
    void updata2() {
        String username = "username=" + Base64.encodeToString(userName.getBytes(), Base64.NO_WRAP | Base64.URL_SAFE);
        String str = username + "&key=3e9bb86c6980c3b79e5b936ce10b9b96";
        String key = MsgDigestUtil.sha256Hex(str);
        String url = webhost + "/index.php/home/Index/achievement?" + username + "&key=" + key;
        if (haveNetworkConnection()){
            webview.loadUrl(url);
        }else {
            webview.loadData("<html><body><h1> NO NETWORK!</h1></body></html>", "text/html", "UTF-8");
        }
    }
}
