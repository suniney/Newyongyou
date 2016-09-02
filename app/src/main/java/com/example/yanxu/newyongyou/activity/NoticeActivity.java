package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.adpter.NoticeListAdapter;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.entity.ProductMessage;
import com.example.yanxu.newyongyou.listener.NoticeClickedListener;
import com.example.yanxu.newyongyou.listener.OnRcvScrollListener;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by yanxu 2016/4/1.
 * 产品公告
 */
@EActivity(R.layout.setting_notice)
public class NoticeActivity extends BaseActivity implements HTTPCons {
    @ViewById
    TextView title;
    @ViewById
    TextView add_address;
    @ViewById
    RecyclerView notice_list;
    @ViewById
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewById
    RelativeLayout notice_noitem;
    private LinearLayoutManager mLayoutManager;
    private NoticeListAdapter mAdapter;
    private Context mContext;
    ArrayList<ProductMessage> productMessages;

    @AfterViews
    void init() {
        mContext = NoticeActivity.this;
        getNotice();
        notice_noitem.setVisibility(View.GONE);
        notice_list.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        notice_list.setLayoutManager(mLayoutManager);
        notice_list.setHasFixedSize(true);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        notice_list.setItemAnimator(new DefaultItemAnimator());
        OnRcvScrollListener listener = new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                // TODO Auto-generated method stub
                super.onBottom();
//                Toast.makeText(mContext, "到底部", Toast.LENGTH_SHORT).show();
            }
        };
        notice_list.setOnScrollListener(listener);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Toast.makeText(mContext, "正在刷新", Toast.LENGTH_SHORT).show();

                // TODO Auto-generated method stub
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        getNotice();
                        // TODO Auto-generated method stub
//                        Toast.makeText(mContext, "刷新完成", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });


    }


    private void getNotice() {
        MyOkHttpUtils.okhttpGet(this, get_notice_action, new CommonCallback() {
                    @Override
                    public void onResponse(Common response,int id) {
                        boolean isSucess = response.isSuccess();
                        if (isSucess) {
                            String s1 = String.valueOf(response.getErrors().get(0));
                            if ("null".equals(s1)) {
                                notice_noitem.setVisibility(View.VISIBLE);
                            } else {
                                productMessages = new Gson().fromJson(s1, new TypeToken<ArrayList<ProductMessage>>() {
                                }.getType());
                                mAdapter = new NoticeListAdapter(productMessages, new NoticeClickedListener() {
                                    @Override
                                    public void onNoticeClick(String id) {
                                        String url = webhost + "/index.php/home/Index/announcement?id=" + id;
                                        ShowWebViewActivity_.intent(mContext).extra("url", url).extra("title","产品公告").
                                        start();
                                    }

                                });
                                notice_list.setAdapter(mAdapter);
                            }

                        } else {
                            Toast.makeText(mContext, String.valueOf(response.getErrors().get(0)), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

        );
    }

    @Click
    void iv_back() {
        this.finish();
    }
}
