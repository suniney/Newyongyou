package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.adpter.MessageMsgListAdapter;
import com.example.yanxu.newyongyou.adpter.MessageNoticeListAdapter;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.entity.Notice;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 * 首页消息盒子
 */
@EActivity(R.layout.message)
public class MessageActivity extends BaseActivity implements HTTPCons {
    @ViewById
    RecyclerView message_list;
    @ViewById
    Button btn_notice;
    @ViewById
    Button btn_msg;
    @ViewById
    RelativeLayout notice_noitem;
    @ViewById
    ScrollView item;
    private LinearLayoutManager mLayoutManager;
    private MessageMsgListAdapter mAdapter;
    private MessageNoticeListAdapter noticeAdapter;
    private boolean isFirstLogin = false;
    Context mContext;
    ArrayList<Notice> notice;

    @AfterViews
    void init() {
        mContext = MessageActivity.this;
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        message_list.setLayoutManager(mLayoutManager);
        message_list.setHasFixedSize(true);
        message_list.setItemAnimator(new DefaultItemAnimator());
        getNotice();
    }


    @Click
    void btn_notice() {
        btn_notice.setTextColor(getResources().getColor(R.color.white));
        btn_notice.setBackgroundColor(getResources().getColor(R.color.black_normal));
        btn_msg.setTextColor(getResources().getColor(R.color.black_normal));
        btn_msg.setBackgroundColor(getResources().getColor(R.color.white_message));
        getNotice();
    }

    private void readMessage(String messageId) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("messageId", messageId);
        MyOkHttpUtils.okhttpPost(mContext, readMessage_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
//                if (isSucess) {
//                    String s1 = String.valueOf(response.getErrors().get(0));
//
//                } else {
//                    String a = String.valueOf(response.getErrors().get(0));
//                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void getNotice() {
        MyOkHttpUtils.okhttpGet(this, searchAnnouncement_action, new CommonCallback() {
                    @Override
                    public void onResponse(Common response,int id) {
                        boolean isSucess = response.isSuccess();
                        if (isSucess) {
                            String s1 = String.valueOf(response.getErrors().get(0));
                            if ("null".equals(s1)) {
                                notice_noitem.setVisibility(View.VISIBLE);
                                item.setVisibility(View.GONE);
                            } else {
                                notice_noitem.setVisibility(View.GONE);
                                item.setVisibility(View.VISIBLE);
                                notice = new Gson().fromJson(s1, new TypeToken<ArrayList<Notice>>() {
                                }.getType());
                                noticeAdapter = new MessageNoticeListAdapter(notice);
                                message_list.setAdapter(noticeAdapter);
                                noticeAdapter.setOnItemClickListener(new MessageNoticeListAdapter.OnRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, String data) {
                                        String url = webhost + "/index.php/home/Index/message?id=" + data;
                                        ShowWebViewActivity_.intent(mContext).extra("url", url).extra("url", url).extra("title","公告").start();
                                    }
                                });
                                MessageActivity.this.setResult(BaseActivity.RESULT_MSG);
                            }
                        } else {
                            Toast.makeText(mContext, String.valueOf(response.getErrors().get(0)), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    @Click
    void btn_msg() {
        isFirstLogin = SharedPreferencesUtil.getValueByKey(mContext, "isFirstLogin", false);
        if (isFirstLogin) {
            btn_msg.setTextColor(getResources().getColor(R.color.white));
            btn_msg.setBackgroundColor(getResources().getColor(R.color.black_normal));
            btn_notice.setTextColor(getResources().getColor(R.color.black_normal));
            btn_notice.setBackgroundColor(getResources().getColor(R.color.white_message));
            getMsg();
        } else {
            LoginActivity_.intent(this).extra("from", "4").start();
            finish();
        }
    }

    private void getMsg() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mContext, "userId", ""));
        MyOkHttpUtils.okhttpPost(mContext, searchMessage_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    String s1 = String.valueOf(response.getErrors().get(0));
                    if ("null".equals(s1)) {
                        notice_noitem.setVisibility(View.VISIBLE);
                        item.setVisibility(View.GONE);
                    } else {
                        notice_noitem.setVisibility(View.GONE);
                        item.setVisibility(View.VISIBLE);
                        notice = new Gson().fromJson(s1, new TypeToken<ArrayList<Notice>>() {
                        }.getType());
                        noticeAdapter = new MessageNoticeListAdapter(notice);
                        message_list.setAdapter(noticeAdapter);
                        noticeAdapter.setOnItemClickListener(new MessageNoticeListAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, String data) {
                                readMessage(data);
                                String url = webhost + "/index.php/home/Index/message?id=" + data;
                                ShowWebViewActivity_.intent(mContext).extra("url", url).extra("title","消息").start();
                                MessageActivity.this.setResult(BaseActivity.RESULT_MSG);
                            }
                        });
                    }
                } else {
                    String a = String.valueOf(response.getErrors().get(0));
                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Click
    void ll_back() {
        finish();
    }
}
