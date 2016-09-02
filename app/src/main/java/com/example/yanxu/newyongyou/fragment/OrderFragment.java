package com.example.yanxu.newyongyou.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.activity.BaseActivity;
import com.example.yanxu.newyongyou.activity.ChangeAddressActivity_;
import com.example.yanxu.newyongyou.activity.MainActivity_;
import com.example.yanxu.newyongyou.activity.OrderActivity_;
import com.example.yanxu.newyongyou.activity.OrderDetailActivity_;
import com.example.yanxu.newyongyou.activity.UpLoadPicActivity_;
import com.example.yanxu.newyongyou.activity.UploadSignActivity_;
import com.example.yanxu.newyongyou.adpter.MyOrderAdapter;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.entity.MyOrder;
import com.example.yanxu.newyongyou.entity.OrderPage;
import com.example.yanxu.newyongyou.listener.OnRcvScrollListener;
import com.example.yanxu.newyongyou.listener.OrderClickedListener;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.utils.SharedPreferencesUtil;
import com.example.yanxu.newyongyou.view.dialog.CustomDialog;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 */
@EFragment(R.layout.fg_order)
public class OrderFragment extends Fragment implements HTTPCons {
    @ViewById
    RecyclerView recyclerView;
    @ViewById
    RelativeLayout order_num;
    @ViewById
    RelativeLayout finish_order_num;
    @ViewById
    RelativeLayout money_order_num;
    @ViewById
    RelativeLayout noitem;
    @ViewById
    Button to_order;
    @ViewById
    SwipeRefreshLayout swipeRefreshLayout;
    private MyOrderAdapter adapter;
    List<MyOrder> myOrder;

    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }
    @AfterViews
    void init() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        getData();
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Toast.makeText(mActivity, "正在刷新", Toast.LENGTH_SHORT).show();

                // TODO Auto-generated method stub
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        getData();
                        // TODO Auto-generated method stub
//                        Toast.makeText(mActivity, "刷新完成", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    private void getData() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userId", SharedPreferencesUtil.getValueByKey(mActivity, "userId", ""));
        MyOkHttpUtils.okhttpPost(mActivity, orderNum_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    OrderPage orderPage = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), OrderPage.class);
                    //在程序中动态加载以上布局。
                    setView(order_num, orderPage.getAllOrders());
                    setView(finish_order_num, orderPage.getSuccessOrders());
                    setView(money_order_num, orderPage.getUnpaidOrders());
                    myOrder = orderPage.getMyOrder();
                    if (myOrder.size() == 0) {
                        noitem.setVisibility(View.VISIBLE);
                    } else {
                        noitem.setVisibility(View.GONE);
                        adapter = new MyOrderAdapter(mActivity, new OrderClickedListener() {
                            @Override
                            public void onCancelClick(final String id) {
                                CustomDialog.Builder builder = new CustomDialog.Builder(mActivity);
                                builder.setTitle("确认取消预约么？");
                                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("确认",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                cancelOrder(id);
                                                dialog.dismiss();
                                            }
                                        });
                                builder.create().show();
                            }

                            @Override
                            public void onSignClick(String id) {
                                UploadSignActivity_.intent(mActivity).extra("orderId", id).startForResult(BaseActivity.RESULT_ORDER);
                            }

                            @Override
                            public void onMoneyClick(String id) {
                                UpLoadPicActivity_.intent(mActivity).extra("orderId", id).startForResult(BaseActivity.RESULT_ORDER);
                            }

                            @Override
                            public void onOrderClick(String proiId, String orderId,String addrId) {
                                OrderActivity_.intent(mActivity).extra("proId", proiId).extra("orderId", orderId).extra("readdrId", addrId).startForResult(BaseActivity.RESULT_ORDER);
                            }
//                            @Override
//                            public void onOrderClick(String id) {
//                                OrderActivity_.intent(mActivity).extra("proId", id).startForResult(BaseActivity.RESULT_ORDER);
////                                ChangeAddressActivity_.intent(mActivity).start();
//                            }

                            @Override
                            public void onAddress(String id) {
                                ChangeAddressActivity_.intent(mActivity).extra("mytitle", "修改合同地址").extra("addrId", id).startForResult(BaseActivity.RESULT_ORDER);
                            }

                            @Override
                            public void onLayoutClick(String orderId, String addrId) {
                                OrderDetailActivity_.intent(mActivity).extra("addrId", addrId).extra("orderId", orderId).startForResult(BaseActivity.RESULT_ORDER);
                            }
                        }, myOrder);
                        //设置动画
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        OnRcvScrollListener listener = new OnRcvScrollListener() {

                            @Override
                            public void onBottom() {
                                // TODO Auto-generated method stub
                                super.onBottom();
//                                Toast.makeText(mActivity, "到底部", Toast.LENGTH_SHORT).show();
                            }

                        };
                        recyclerView.setOnScrollListener(listener);
                        recyclerView.setAdapter(adapter);
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
    void to_order() {
        MainActivity_.intent(mActivity).flags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT).start();
//        CommonUtils.sendBroadCast(mActivity, "updata", "from", "0");
    }

    @OnActivityResult(BaseActivity.RESULT_ORDER)
    void onResultSign(int resultCode) {
        if (resultCode == BaseActivity.RESULT_ORDER) {
            getData();
        }
    }

    @Receiver(actions = "com.yinduo.yongyou.orderupdata")
    void updata() {
        getData();
    }

    @Receiver(actions = "com.yinduo.yongyou.loginupdata")
    void updata1() {
        getData();
    }

    private void cancelOrder(String id) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("id", id);
        MyOkHttpUtils.okhttpPost(mActivity, cancelOrder_action, map, new CommonCallback() {
                    @Override
                    public void onResponse(Common response,int id) {
                        boolean isSucess = response.isSuccess();
                        if (isSucess) {
                            CommonUtils.ToastShow(mActivity,"取消预约成功");
                            getData();
                        }
//                        else {
//                            String a = String.valueOf(response.getErrors().get(0));
//                            Toast.makeText(mActivity, a.toString(), Toast.LENGTH_SHORT).show();
//                        }
                    }
                }

        );
    }

    private void setView(RelativeLayout relativeLayout, int num) {
        TextView num_one;
        TextView num_two;
        if (num < 10) {
            LayoutInflater flater = LayoutInflater.from(mActivity);
            View view = flater.inflate(R.layout.num_one, null);
            num_one = (TextView) view.findViewById(R.id.num_one);
            num_one.setText(String.valueOf(num));
            relativeLayout.addView(view);
        } else {
            LayoutInflater flater = LayoutInflater.from(mActivity);
            View view = flater.inflate(R.layout.num_two, null);
            num_two = (TextView) view.findViewById(R.id.num_two);
            if (num > 99) {
                num_two.setText("···");//·
            } else {
                num_two.setText(String.valueOf(num));
            }
            relativeLayout.addView(view);

        }
    }

}





