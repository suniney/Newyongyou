package com.example.yanxu.newyongyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.entity.Share;
import com.example.yanxu.newyongyou.utils.DateUtil;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yd on 2016/3/31.
 * 商品详情
 */
@EActivity(R.layout.product_detail)
public class ProductDetailActivity extends Activity implements HTTPCons {
    @ViewById
    WebView product_webview;
    @ViewById
    Button btn_appoint;
    @ViewById
    TextView title;
    @ViewById
    TextView tv_openDate;
    @Extra
    String proId;
    private boolean isFirstLogin = false;
    Context mContext;
    WebChromeClient wvcc;
    private Handler handler = new Handler();
    // 首先在您的Activity中添加如下成员变量
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    private SocializeListeners.SnsPostListener snsPostListener;
    String str;
    @Extra
    String openDate;
    String shareUrl;
    String shareContent;
    String shareTitle;

    @AfterViews
    void init() {
        mContext = ProductDetailActivity.this;
        WebSettings webSettings = product_webview.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(false);
        wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String webtitle) {
                super.onReceivedTitle(view, webtitle);
                title.setText(webtitle);
            }
        };
        product_webview.setWebChromeClient(wvcc);
        str = webhost + "/Home/Index/product/id/";
        product_webview.loadUrl(str + proId);
        tv_openDate.setText(DateUtil.timeStamp2Date(openDate, null));
    }

    @Click
    void btn_appoint() {
        isFirstLogin = SharedPreferencesUtil.getValueByKey(mContext, "isFirstLogin", false);
        if (isFirstLogin) {
            OrderActivity_.intent(this).extra("proId", proId).start();
            finish();
        } else {
            LoginActivity_.intent(this).extra("from", "3").extra("proId", proId).start();
        }
    }

    @Click
    void share() {
        getShare();

    }

    private void getShare() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("proId", proId);
        MyOkHttpUtils.okhttpPost(mContext, share_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    String s1 = String.valueOf(response.getErrors().get(0));
                    Share share = new Gson().fromJson(s1, Share.class);
                    shareUrl = share.getUrl();
                    shareContent = share.getContent();
                    shareTitle = share.getTitle();
                    handler.post(new Runnable() {
                        public void run() {
                            snsPostListener = new SocializeListeners.SnsPostListener() {
                                @Override
                                public void onStart() {
                                }
                                @Override
                                public void onComplete(SHARE_MEDIA platform, int stCode, SocializeEntity entity) {
                                    if (stCode == 200) {
                                        Toast.makeText(ProductDetailActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                    }
                                }
                            };
                            mController.registerListener(snsPostListener);
                            //默认分享列表中存在的平台如果需要删除，则调用下面的代码：
                            mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
                            configPlatforms();
                            setShareContent();
                            mController.openShare(ProductDetailActivity.this, false);

                        }
                    });
                } else {
                    String a = String.valueOf(response.getErrors().get(0));
                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加微信、微信朋友圈平台
        addWXPlatform();
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx500b5aa1a5178c53";
        String appSecret = "c71b0745196389538eda6852085af428";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(ProductDetailActivity.this, appId, appSecret);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(ProductDetailActivity.this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setShareContent() {
        // 配置SSO
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        UMImage localImage = new UMImage(ProductDetailActivity.this, R.mipmap.yongyou);
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(shareContent);
        weixinContent.setTitle(shareTitle);
        weixinContent.setTargetUrl(shareUrl);
        weixinContent.setShareMedia(localImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(shareContent);
        circleMedia.setTitle(shareTitle);
        circleMedia.setShareMedia(localImage);
        circleMedia.setTargetUrl(shareUrl);
        mController.setShareMedia(circleMedia);
//
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setTitle(shareTitle);
        sinaContent.setShareContent(shareContent);
        sinaContent.setShareMedia(localImage);
        sinaContent.setTargetUrl(shareUrl);
        mController.setShareMedia(sinaContent);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    @Click
    void btn_back() {
        onBackPressed();
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data);
        this.finish();
    }
}

