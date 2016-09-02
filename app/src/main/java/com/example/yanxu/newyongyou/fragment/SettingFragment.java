package com.example.yanxu.newyongyou.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.activity.BaseActivity;
import com.example.yanxu.newyongyou.activity.LoginActivity_;
import com.example.yanxu.newyongyou.activity.MoneyActivity_;
import com.example.yanxu.newyongyou.activity.NameActivity_;
import com.example.yanxu.newyongyou.activity.NormalSettingActivity_;
import com.example.yanxu.newyongyou.activity.NoticeActivity_;
import com.example.yanxu.newyongyou.activity.PicturePopWindow_;
import com.example.yanxu.newyongyou.activity.UserCenterActivity_;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.callBack.ImageCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.entity.MyInfo;
import com.example.yanxu.newyongyou.entity.Share;
import com.example.yanxu.newyongyou.utils.CircleImageView;
import com.example.yanxu.newyongyou.utils.CommonUtils;
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
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by yanxu 2016/4/1.
 */
@EFragment(R.layout.fg_setting)
public class SettingFragment extends BaseFragment implements HTTPCons {

    @ViewById
    TextView myInfo_name;
    @ViewById
    TextView myInfo_money;
    @ViewById
    CircleImageView iv_headimg;
    @ViewById
    RelativeLayout money;
    @ViewById
    TextView money_tv_2;
    @ViewById
    ImageView iv_money;
    @ViewById
    RelativeLayout setting;
    @ViewById
    TextView setting_tv;
    @ViewById
    ImageView iv_setting;
    @ViewById
    RelativeLayout usercenter;
    @ViewById
    TextView usercenter_tv;
    @ViewById
    ImageView iv_usercenter;
    @ColorRes
    String text_money;
    String shareUrl;
    String shareContent;
    String shareTitle;
    private boolean isFirstLogin = false;
    private boolean noLogin;
    MyInfo myInfo;
    private Handler handler = new Handler();
    // 首先在您的Activity中添加如下成员变量
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    private SocializeListeners.SnsPostListener snsPostListener;
    String str;
    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }
    @AfterViews
    void init() {

        isFirstLogin = SharedPreferencesUtil.getValueByKey(mActivity, "isFirstLogin", false);
        if (!isFirstLogin) {
            noLogin(true);
        } else {
            myInfo();

        }
    }

    @Click
    void referShare() {
        if (haveNetworkConnection()){
            if (!"".equals(SharedPreferencesUtil.getValueByKey(mActivity, "userId", ""))) {
                getShare();
            } else {
                LoginActivity_.intent(mActivity).extra("from", "5").start();
            }
        }
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
        UMWXHandler wxHandler = new UMWXHandler(mActivity, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setShareContent() {
        // 配置SSO
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        UMImage localImage = new UMImage(mActivity, R.mipmap.yongyou);
//
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    private void getShare() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("regMobile", myInfo.getRegMobile());
        MyOkHttpUtils.okhttpPost(mActivity, userCenterShare_action, map, new CommonCallback() {
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
                            //默认分享列表中存在的平台如果需要删除，则调用下面的代码：
                            snsPostListener = new SocializeListeners.SnsPostListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onComplete(SHARE_MEDIA platform, int stCode,
                                                       SocializeEntity entity) {
                                    if (stCode == 200) {
                                        Toast.makeText(mActivity, "分享成功", Toast.LENGTH_SHORT)
                                                .show();
                                    } else {
                                    }
                                }
                            };
                            mController.registerListener(snsPostListener);
                            //默认分享列表中存在的平台如果需要删除，则调用下面的代码：
                            mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
                            configPlatforms();
                            setShareContent();
                            mController.openShare(mActivity, false);
                        }
                    });

                } else {
                    String a = String.valueOf(response.getErrors().get(0));
                    Toast.makeText(mActivity, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void myInfo() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        if (!"".equals(SharedPreferencesUtil.getValueByKey(mActivity, "userId", ""))) {
            map.put("userId", SharedPreferencesUtil.getValueByKey(mActivity, "userId", ""));
            MyOkHttpUtils.okhttpPost(mActivity, myInfo_action, map, new CommonCallback() {
                @Override
                public void onResponse(Common response,int id) {
                    boolean isSucess = response.isSuccess();
                    if (isSucess) {
                        String string = String.valueOf(response.getErrors().get(0));
                        myInfo = new Gson().fromJson(string, MyInfo.class);
                        if (myInfo.getAllIncome() == null) {
                            myInfo_money.setText("￥0.00");
                        } else {
                            myInfo_money.setText(mActivity.getResources().getString(R.string.money1, myInfo.getAllIncome()));
                        }
                        myInfo_name.setText(myInfo.getName());
                        if (myInfo.getPhotoUrl() != null) {
                            MyOkHttpUtils.getImage(myInfo.getPhotoUrl(), new ImageCallback() {
                                @Override
                                public void onResponse(Bitmap response,int id) {
                                    iv_headimg.setImageBitmap(response);
                                }
                            });
                        }

                    }
// else {
//
//                        String a = String.valueOf(response.getErrors().get(0));
//                        Toast.makeText(mActivity, a.toString(), Toast.LENGTH_SHORT).show();
//                    }
                }
            });
        }
    }

    private void noLogin(boolean noLogin) {
        if (noLogin) {
            iv_money.setImageDrawable(getResources().getDrawable(R.drawable.setting_money_icon_nocheck));
            myInfo_money.setTextColor(getResources().getColor(R.color.text_nocheck));
            money_tv_2.setTextColor(getResources().getColor(R.color.text_nocheck));
            iv_usercenter.setImageDrawable(getResources().getDrawable(R.drawable.setting_user_icon_nocheck));
            usercenter_tv.setTextColor(getResources().getColor(R.color.text_nocheck));
        } else {
            iv_money.setImageDrawable(getResources().getDrawable(R.drawable.setting_money_img_selector));
            myInfo_money.setTextColor(getResources().getColor(R.color.setting_money_text_selector));
            money_tv_2.setTextColor(getResources().getColor(R.color.setting_money_text_selector));
            iv_usercenter.setImageDrawable(getResources().getDrawable(R.drawable.setting_user_img_selector));
            usercenter_tv.setTextColor(getResources().getColor(R.color.setting_money_text_selector));
            myInfo();
        }
    }

    public boolean noLogin() {
        return noLogin;
    }

    @Click
    void iv_headimg() {
        if (!isFirstLogin) {
            LoginActivity_.intent(mActivity).extra("from", "5").start();
        } else {
            PicturePopWindow_.intent(this).startForResult(BaseActivity.RESULT_SIGN_PHOTO);
        }


    }

    @OnActivityResult(BaseActivity.RESULT_SIGN_PHOTO)
    void onResultPhoto(int resultCode, Intent intent) {
        if (resultCode == BaseActivity.RESULT_SIGN_PHOTO) {
            Bitmap bitmap;
            bitmap = intent.getParcelableExtra("bitmap");
            String photo = intent.getStringExtra("temp");
            uploadHeadPic(photo);
//            iv_headimg.setImageBitmap(bitmap);
        }
    }

    private void uploadHeadPic(String photo) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mActivity, "userId", ""));
        map.put("pic", photo);
        MyOkHttpUtils.okhttpPost(mActivity, uploadHeadPic_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    myInfo();
                    CommonUtils.sendBroadCast(mActivity, "com.yinduo.yongyou.photoupdata", null, null);
                }
            }

        });
    }

    @Click
    void money() {
        if (!isFirstLogin) {
            Toast.makeText(mActivity, "请登录账号", Toast.LENGTH_SHORT).show();
        } else {
            MoneyActivity_.intent(mActivity).start();
        }

    }

    @Click
    void setting() {
        NormalSettingActivity_.intent(mActivity).start();
    }

    @Click
    void product() {
        NoticeActivity_.intent(mActivity).start();
    }

    @Click
    void usercenter() {
        if (!isFirstLogin) {
            Toast.makeText(mActivity, "请登录账号", Toast.LENGTH_SHORT).show();
        } else {
            UserCenterActivity_.intent(mActivity).start();
        }

    }

    @Receiver(actions = "com.yinduo.yongyou.loginupdata")
    void updata() {
        isFirstLogin = SharedPreferencesUtil.getValueByKey(mActivity, "isFirstLogin", false);
        if (!isFirstLogin) {
            noLogin(true);
            iv_headimg.setImageResource(R.drawable.avatar);
            myInfo_name.setText("未登录");
            myInfo_money.setText("￥0.00");
        } else {
            noLogin(false);
            myInfo();
        }

    }

    @Click
    void myInfo_name() {
        isFirstLogin = SharedPreferencesUtil.getValueByKey(mActivity, "isFirstLogin", false);
        if (!isFirstLogin) {
            LoginActivity_.intent(mActivity).extra("from", "5").start();
        } else {
            NameActivity_.intent(mActivity).extra("name", myInfo.getName()).extra("regMobile", myInfo.getRegMobile()).startForResult(BaseActivity.RESULT_SET);
        }

    }

    @OnActivityResult(BaseActivity.RESULT_SET)
    void onResultSet(int resultCode, Intent intent) {
        if (resultCode == BaseActivity.RESULT_SET) {
            myInfo();
        }
    }

    @Receiver(actions = "com.yinduo.yongyou.photoupdata")
    protected void updataphoto() {
        myInfo();
    }
//       setGesture = (LinearLayout) view.findViewById(R.id.setGesture);
//        setGesture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =new Intent(mActivity, CreateGestureActivity.class);
//                intent.putExtra("from","setGesture");
//                startActivity(intent);
//
//            }
//        });
}
