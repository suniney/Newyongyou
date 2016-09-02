package com.example.yanxu.newyongyou.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.activity.BaseActivity;
import com.example.yanxu.newyongyou.activity.LoginActivity_;
import com.example.yanxu.newyongyou.activity.MessageActivity_;
import com.example.yanxu.newyongyou.activity.ProductDetailActivity_;
import com.example.yanxu.newyongyou.activity.ShowWebViewActivity_;
import com.example.yanxu.newyongyou.adpter.BannerFragmentAdapter;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.callBack.ImageCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.entity.HomeBanner;
import com.example.yanxu.newyongyou.entity.HomePage;
import com.example.yanxu.newyongyou.entity.HomeProductInfo;
import com.example.yanxu.newyongyou.listener.OnBannerClickedListener;
import com.example.yanxu.newyongyou.utils.CircleImageView;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 */
@EFragment(R.layout.fg_home)
@SuppressLint("SetJavaScriptEnabled")
public class HomeFragment extends Fragment implements HTTPCons {
    private android.os.Handler mHandler;
    private final int WHEEL = 1;
    private final int UPDATA = 11;
    private final int WHEEL_WAIT = WHEEL + 1;
    private int time = 2000; // 默认轮播时间
    private boolean autoScroll = false;
    SettingFragment forthFragment;
    @ViewById
    ViewPager bannerViewPager;
    @ViewById
    CircleImageView home_icon;
    @ViewById
    RelativeLayout userinfo_message_num_point;
    private int currentPosition = 0; // 轮播当前位置
    //    private boolean isScrolling = false; // 滚动框是否滚动着
    private boolean isCycle = false; // 是否循环
    private boolean isWheel = false; // 是否轮播
    private long releaseTime = 0; // 手指松开、页面不滚动时间，防止手机松开后短时间进行切换
    List<HomeBanner> homeBanner;
    List<HomeProductInfo> homeProductInfos;
    private boolean isFirstLogin = false;
    WebChromeClient wvcc;
    @ViewById
    WebView webview;
    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @AfterViews
    void init() {
        isFirstLogin = SharedPreferencesUtil.getValueByKey(mActivity, "isFirstLogin", false);
        firstHome();
        mHandler = new android.os.Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == UPDATA && homeBanner.size() != 0) {
                    List<BannerFragment> contents = new ArrayList<BannerFragment>();
                    for (int i = 0; i < homeBanner.size(); i++) {
                        String bannerUrl = homeBanner.get(i).getPhotoUrl();
                        String actUrl = homeBanner.get(i).getActUrl();
                        System.out.println(bannerUrl);
                        // 新建Fragment的实例对象，并设置参数传递到Fragment中
                        Bundle args = new Bundle();
                        BannerFragment content = new BannerFragment(
                                mActivity, new OnBannerClickedListener() {
                            @Override
                            public void onItemClick(View v, String url) {
                                if (url != null && !"".equals(url))
                                    ShowWebViewActivity_.intent(mActivity).extra("url", url).start();

                            }
                        });
                        if (bannerUrl != null) {
                            args.putString("bannerUrl", bannerUrl);
                        }
                        args.putString("actUrl", actUrl);
                        content.setArguments(args);
                        contents.add(content);
                        // 这个getSupportFragmentManager只有activity继承FragmentActivity才会有
                    }


                    BannerFragmentAdapter adapter = new BannerFragmentAdapter(mActivity.getSupportFragmentManager(), contents);
                    bannerViewPager.setAdapter(adapter);
                    bannerViewPager.setCurrentItem(0);// 设置当前显示标签页为第一页
                    bannerViewPager.setOffscreenPageLimit(homeBanner.size());
                    bannerViewPager.setOnPageChangeListener(new BannerOnPageChangedListener(bannerViewPager));
                    // 设置循环
                    setCycle(true);
                    //设置轮播
                    setWheel(true);
                }
                if (msg.what == WHEEL && homeBanner.size() != 0) {
                    if (!autoScroll) {
                        int max = homeBanner.size();
                        int position = (currentPosition) % homeBanner.size();
                        currentPosition = currentPosition + 1;
                        bannerViewPager.setCurrentItem(position, true);
                        if (position == max) { // 最后一页时回到第一页
                            currentPosition = 0;
                            bannerViewPager.setCurrentItem(1, false);
                        }
                    }
                    releaseTime = System.currentTimeMillis();
                    mHandler.removeCallbacks(runnable);
                    mHandler.postDelayed(runnable, time);
                    return;
                }
                if (msg.what == WHEEL_WAIT && homeBanner.size() != 0) {
                    mHandler.removeCallbacks(runnable);
                    mHandler.postDelayed(runnable, time);
                }

            }
        };
        WebSettings webSettings = webview.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(false);
        webview.setVerticalScrollbarOverlay(true);
//        设置WebView支持JavaScript
//        if (CommonUtils.isNetworkAvailable(mActivity)) {
//            webview.loadData("<html><body><h1> NO NETWORK!</h1></body></html>", "text/html", "UTF-8");
//        } else {
//            webview.loadUrl("http://121.43.118.86:10220/Home/Product/stock");
//        }
        webview.loadUrl("http://121.43.118.86:10220/Home/Product/stock");
        webview.addJavascriptInterface(new JsInterface(mActivity), "index");
        //添加客户端支持
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
    }

    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void link(String id, String time) {
            ProductDetailActivity_.intent(mActivity).extra("proId", id).extra("openDate", time).start();
        }
    }


    @Receiver(actions = "com.yinduo.yongyou.loginupdata")
    void updata() {
        firstHome();
    }

    @Receiver(actions = "com.yinduo.yongyou.photoupdata")
    void updataphoto() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mActivity, "userId", ""));
        map.put("type", "1");
        MyOkHttpUtils.okhttpPost(mActivity, get_home_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response, int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    HomePage homePage = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), HomePage.class);
                    if (homePage.getHeadUrl() == null) {
                        home_icon.setImageResource(R.drawable.avatar);
                    } else {
                        MyOkHttpUtils.getImage(homePage.getHeadUrl(), new ImageCallback() {
                            @Override
                            public void onResponse(Bitmap response, int id) {
                                home_icon.setImageBitmap(response);
                                CommonUtils.saveAvatar(response);
                            }
                        });
                    }
                }
//                else {
//                    String a = String.valueOf(response.getErrors().get(0));
//                    Toast.makeText(mActivity, a.toString(), Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    @Click
    void home_icon() {
        if (!isFirstLogin) {
            LoginActivity_.intent(mActivity).extra("from", "0").start();
        } else {

        }
    }

    @Click
    void userinfo_message_btn() {
        MessageActivity_.intent(mActivity).startForResult(BaseActivity.RESULT_MSG);
    }

    @OnActivityResult(BaseActivity.RESULT_MSG)
    void onResultMsg(int resultCode) {
        if (resultCode == BaseActivity.RESULT_MSG) {
            firstHome();
        }
    }

    private void firstHome() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        if ("".equals(SharedPreferencesUtil.getValueByKey(mActivity, "userId", ""))) {
            map.put("type", "1");
        } else {
            map.put("userId", SharedPreferencesUtil.getValueByKey(mActivity, "userId", ""));
            map.put("type", "1");
        }
        MyOkHttpUtils.okhttpPost(mActivity, get_home_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response, int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    String s1 = String.valueOf(response.getErrors().get(0));
                    HomePage homePage = new Gson().fromJson(s1, HomePage.class);
                    homeBanner = homePage.getHomeBanners();
                    if ("1".equals(homePage.getMessageStatus())) {
                        userinfo_message_num_point.setVisibility(View.INVISIBLE);
                    } else {
                        userinfo_message_num_point.setVisibility(View.VISIBLE);
                    }
                    homeProductInfos = homePage.getHomeProductInfos();
                    mHandler.sendEmptyMessage(UPDATA);
//                    mAdapter = new ProductListAdapter(mActivity, homeProductInfos, new HomeClickedListener() {
//                        @Override
//                        public void onItemClick(String id, String openDate) {
//                            ProductDetailActivity_.intent(mActivity).extra("proId", id).extra("openDate", openDate).start();
//                        }
//                    });
//                    product_list.setAdapter(mAdapter);
                    if (homePage.getHeadUrl() == null) {
                        home_icon.setImageResource(R.drawable.avatar);
                    } else {
                        MyOkHttpUtils.getImage(homePage.getHeadUrl(), new ImageCallback() {
                            @Override
                            public void onResponse(Bitmap response, int id) {
                                home_icon.setImageBitmap(response);
                                CommonUtils.saveAvatar(response);
                            }
                        });
                    }
                }
//                else {
//                    String a = String.valueOf(response.getErrors().get(0));
//                    Toast.makeText(mActivity, a.toString(), Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

//    private void getHome(String type) {
//        Map<String, String> map = new LinkedHashMap<String, String>();
//        if ("".equals(SharedPreferencesUtil.getValueByKey(mActivity, "userId", ""))) {
//            map.put("type", type);
//        } else {
//            map.put("userId", SharedPreferencesUtil.getValueByKey(mActivity, "userId", ""));
//            map.put("type", type);
//        }
//        MyOkHttpUtils.okhttpPost(mActivity, get_home_action, map, new CommonCallback() {
//            @Override
//            public void onResponse(Common response) {
//                boolean isSucess = response.isSuccess();
//                if (isSucess) {
//                    HomePage homePage = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), HomePage.class);
//                    homeProductInfos = homePage.getHomeProductInfos();
//                    mAdapter = new ProductListAdapter(mActivity, homeProductInfos, new HomeClickedListener() {
//                        @Override
//                        public void onItemClick(String id, String openDate) {
//                            ProductDetailActivity_.intent(mActivity).extra("proId", id).extra("openDate", openDate).start();
//                        }
//                    });
//                    product_list.setAdapter(mAdapter);
//
//                }
////                else {
////                    String a = String.valueOf(response.getErrors().get(0));
////                    Toast.makeText(mActivity, a.toString(), Toast.LENGTH_SHORT).show();
////                }
//            }
//        });
//    }

    public class BannerOnPageChangedListener implements ViewPager.OnPageChangeListener {
        private ViewPager view;
        private int offset;
        private int currIndex;
        private ImageView cursor;

        public BannerOnPageChangedListener(ViewPager view) {
            this.view = view;

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            if (arg0 == 1) { // viewPager在滚动
                autoScroll = true;
                return;
            } else if (arg0 == 0) { // viewPager滚动结束
                if (bannerViewPager != null)
//                    bannerViewPager.setScrollable(true);
                    releaseTime = System.currentTimeMillis();
                bannerViewPager.setCurrentItem(currentPosition, false);
            }
            autoScroll = false;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {

            BannerFragmentAdapter bf = (BannerFragmentAdapter) view
                    .getAdapter();
            BannerFragment b = bf.getItem(arg0);

            Animation animation = new TranslateAnimation(currentPosition * offset,
                    arg0 * offset, 0, 0);// 平移动画
            currentPosition = arg0;
            animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
            animation.setDuration(200);// 动画持续时间0.2秒

        }

    }


    /**
     * 是否循环，默认不开启，开启前，请将views的最前面与最后面各加入一个视图，用于循环
     *
     * @param isCycle 是否循环
     */
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    /**
     * 是否处于循环状态
     */
    public boolean isCycle() {
        return isCycle;
    }

    /**
     * 设置是否轮播，默认不轮播,轮播一定是循环的
     *
     * @param isWheel
     */
    public void setWheel(boolean isWheel) {
        this.isWheel = isWheel;
        isCycle = true;
        if (isWheel) {
            mHandler.postDelayed(runnable, time);
        }
    }

    /**
     * 是否处于轮播状态
     *
     * @return
     */
    public boolean isWheel() {
        return isWheel;
    }

    final Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (mActivity != null && !mActivity.isFinishing()
                    && isWheel) {
                long now = System.currentTimeMillis();
                // 检测上一次滑动时间与本次之间是否有触击(手滑动)操作，有的话等待下次轮播
                if (now - releaseTime > time - 500) {
                    mHandler.sendEmptyMessage(WHEEL);
                } else {
                    mHandler.sendEmptyMessage(WHEEL_WAIT);
                }
            }
        }
    };


}





