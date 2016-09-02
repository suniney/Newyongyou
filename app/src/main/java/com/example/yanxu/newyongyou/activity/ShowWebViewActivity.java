package com.example.yanxu.newyongyou.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.yanxu.newyongyou.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.show_web_view)
@SuppressLint("SetJavaScriptEnabled")
public class ShowWebViewActivity extends BaseActivity {

    @Extra
    String url;
    @Extra
    String title;
    WebChromeClient wvcc;
    private WebView webView;

    @AfterViews
    void init() {
        ((TextView) findViewById(R.id.title)).setText(title);
//        wvcc = new WebChromeClient() {
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                ((TextView) findViewById(R.id.title)).setText(title);
//            }
//
//        };
        webView = (WebView) findViewById(R.id.webView1);
        if (haveNetworkConnection()) {
            startWebView(url);
        } else {
            webView.loadUrl("file:///android_asset/error.html");
        }
    }

    private void startWebView(String url) {
        // 设置setWebChromeClient对象
        webView.setWebChromeClient(wvcc);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        webView.setSaveEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

    }


    @Click
    void btn_back() {
        onBackPressed();
    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            this.finish();

        }
    }
}



