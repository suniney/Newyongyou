package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.entity.OrderDetailPage;
import com.example.yanxu.newyongyou.entity.UserAddr;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.DateUtil;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;
import com.example.yanxu.newyongyou.view.dialog.CustomDialog;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 * 订单详情
 */
@EActivity(R.layout.order_detail)
public class OrderDetailActivity extends BaseActivity implements HTTPCons {
    @ViewById
    ScrollView scrollView;
    @ViewById
    TextView title;
    @ViewById
    TextView status;
    @ViewById
    TextView orderNumb;
    @ViewById
    TextView orderDate;
    @ViewById
    TextView proName;
    @ViewById
    TextView realRame;
    @ViewById
    TextView money;
    @ViewById
    TextView phoneNumb;
    @ViewById
    TextView remark;
    @ViewById
    TextView postStatus;
    @ViewById
    TextView realName;
    @ViewById
    TextView mobile;
    @ViewById
    TextView address;
    @ViewById
    RelativeLayout rl_appont;
    @ViewById
    Button order_cancel;
    @ViewById
    Button order_upload_sign;
    @ViewById
    Button order_upload_money;
    @ViewById
    Button order_addrss;
    @ViewById
    Button order_reorder;
    @ViewById
    Button order_addrss_1;
    @ViewById
    Button order_cancel_1;
    @ViewById
    RelativeLayout rl_appont_1;
    @ViewById
    RelativeLayout to_flow;
    @Extra
    String orderId;
    @Extra
    String addrId;
    Context mContext;
    int statusId;
    OrderDetailPage orderDetailPage;
    UserAddr userAddr;

    @AfterViews
    void init() {
        setTranStatusBar();
        mContext = OrderDetailActivity.this;
        getData();
    }

    @Click
    void order_upload_money() {
        UpLoadPicActivity_.intent(mContext).extra("orderId", orderId).startForResult(BaseActivity.RESULT_ORDER);
    }

    @Click
    void order_addrss() {
        ChangeAddressActivity_.intent(mContext).extra("mytitle", "修改合同地址").extra("addrId", userAddr.getId()).startForResult(BaseActivity.RESULT_ORDER);
    }

    @Click
    void order_addrss_1() {
        ChangeAddressActivity_.intent(mContext).extra("mytitle", "修改合同地址").extra("addrId", userAddr.getId()).startForResult(BaseActivity.RESULT_ORDER);
    }

    @Click
    void order_upload_sign() {
        UploadSignActivity_.intent(mContext).extra("orderId", orderId).startForResult(BaseActivity.RESULT_ORDER);
    }

    @Click
    void order_reorder() {
        finish();
        OrderActivity_.intent(mContext).extra("proId", orderDetailPage.getProId().toString()).extra("orderId", orderId).extra("readdrId", addrId).startForResult(BaseActivity.RESULT_ORDER);
    }

    @Click
    void order_cancel_1() {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setTitle("确认取消预约么？");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cancelOrder(orderId);
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Click
    void order_cancel() {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setTitle("确认取消预约么？");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cancelOrder(orderId);
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void cancelOrder(String id) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("id", id);
        MyOkHttpUtils.okhttpPost(mContext, cancelOrder_action, map, new CommonCallback() {
                    @Override
                    public void onResponse(Common response,int id) {
                        boolean isSucess = response.isSuccess();
                        if (isSucess) {
                            getData();
                            CommonUtils.ToastShow(mContext, "取消成功");
                            OrderDetailActivity.this.setResult(BaseActivity.RESULT_ORDER);
                        } else {
                            String a = String.valueOf(response.getErrors().get(0));
                            Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    @OnActivityResult(BaseActivity.RESULT_ORDER)
    void onResultSign(int resultCode) {
        if (resultCode == BaseActivity.RESULT_ORDER) {
            getData();
        }
    }

    private void getData() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("orderId", orderId);
        map.put("addrId", addrId);
        MyOkHttpUtils.okhttpPost(mContext, orderDetail_action, map, new CommonCallback() {
            @Override
            public void onResponse(Common response,int id) {
                boolean isSucess = response.isSuccess();
                if (isSucess) {
                    orderDetailPage = new Gson().fromJson(String.valueOf(response.getErrors().get(0)), OrderDetailPage.class);
                    statusId = orderDetailPage.getStatus();
                    if (statusId == STATUS_BOOK_UNSUCCESS || statusId == STATUS_CLOSE) {
                        to_flow.setClickable(false);
                    } else {
                        to_flow.setClickable(true);
                    }
                    switch (statusId) {
                        case STATUS_BOOK_UNSUCCESS:
                            status.setText("预约失败");
                            rl_appont_1.setVisibility(View.GONE);
                            rl_appont.setVisibility(View.VISIBLE);
                            order_cancel.setVisibility(View.INVISIBLE);
                            order_upload_money.setVisibility(View.INVISIBLE);
                            order_upload_sign.setVisibility(View.INVISIBLE);
                            order_addrss.setVisibility(View.INVISIBLE);
                            order_reorder.setVisibility(View.VISIBLE);
                            break;
                        case STATUS_BOOK_SUCCESS:
                            status.setText("预约成功");
                            rl_appont_1.setVisibility(View.GONE);
                            rl_appont.setVisibility(View.VISIBLE);
                            order_cancel.setVisibility(View.VISIBLE);
                            order_upload_money.setVisibility(View.VISIBLE);
                            order_upload_sign.setVisibility(View.INVISIBLE);
                            order_addrss.setVisibility(View.VISIBLE);
                            order_reorder.setVisibility(View.INVISIBLE);
                            break;
                        case STATUS_BOOK_CHECK:
                            status.setText("预约待审核");
                            rl_appont_1.setVisibility(View.VISIBLE);
                            rl_appont.setVisibility(View.GONE);
                            break;
                        case STATUS_UPLOAD_CHECK:
                            status.setText("打款凭证审核");
                            rl_appont_1.setVisibility(View.GONE);
                            rl_appont.setVisibility(View.GONE);

                            break;
                        case STATUS_UPLOAD_MONEY:
                            status.setText("重新上传打款凭证");
                            rl_appont.setVisibility(View.VISIBLE);
                            rl_appont_1.setVisibility(View.GONE);
                            order_cancel.setVisibility(View.INVISIBLE);
                            order_upload_money.setVisibility(View.VISIBLE);
                            order_upload_sign.setVisibility(View.INVISIBLE);
                            order_addrss.setVisibility(View.INVISIBLE);
                            order_reorder.setVisibility(View.INVISIBLE);
                            break;

                        case STATUS_FINISH:
                            status.setText("客户完成交易");
                            rl_appont.setVisibility(View.VISIBLE);
                            rl_appont_1.setVisibility(View.GONE);
                            order_cancel.setVisibility(View.INVISIBLE);
                            order_upload_money.setVisibility(View.INVISIBLE);
                            order_upload_sign.setVisibility(View.VISIBLE);
                            order_addrss.setVisibility(View.INVISIBLE);
                            order_reorder.setVisibility(View.INVISIBLE);
                            break;
                        case STATUS_REUPLOAD_SIGN:
                            status.setText("重新上传合同");
                            rl_appont.setVisibility(View.VISIBLE);
                            rl_appont_1.setVisibility(View.GONE);
                            order_cancel.setVisibility(View.INVISIBLE);
                            order_upload_money.setVisibility(View.INVISIBLE);
                            order_upload_sign.setVisibility(View.VISIBLE);
                            order_addrss.setVisibility(View.INVISIBLE);
                            order_reorder.setVisibility(View.INVISIBLE);
                            break;
                        case STATUS_UPLOAD_SIGN:
                            status.setText("合同签署页审核");
                            rl_appont_1.setVisibility(View.GONE);
                            rl_appont.setVisibility(View.GONE);

                            break;
                        case STATUS_BACK_MONEY:
                            status.setText("首次返佣");
                            rl_appont_1.setVisibility(View.GONE);
                            rl_appont.setVisibility(View.GONE);

                            break;
                        case STATUS_RECYCLE_SIGN:
                            status.setText("回收合同原件");
                            rl_appont_1.setVisibility(View.GONE);
                            rl_appont.setVisibility(View.GONE);

                            break;
                        case STATUS_OVER:
                            status.setText("交易结束，并计入存量");
                            rl_appont_1.setVisibility(View.GONE);
                            rl_appont.setVisibility(View.INVISIBLE);
                            break;
                        case STATUS_CLOSE:
                            status.setText("订单关闭");
                            rl_appont_1.setVisibility(View.GONE);
                            rl_appont.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }
                    orderNumb.setText(orderDetailPage.getOrderNumb());
                    orderDate.setText(DateUtil.timeStamp2Date(String.valueOf(orderDetailPage.getOrderDate()), null));
                    proName.setText(orderDetailPage.getProName());
                    realRame.setText(orderDetailPage.getRealRame());
                    money.setText(orderDetailPage.getMoney().toString());
                    phoneNumb.setText(orderDetailPage.getPhoneNumb());
                    remark.setText(orderDetailPage.getRemark());
                    userAddr = orderDetailPage.getUserAddrs();
                    if (orderDetailPage.getPostStatus() == null) {
                        postStatus.setText("未邮寄");
                    } else {
                        switch (orderDetailPage.getPostStatus().intValue()) {
                            case 2:
                                postStatus.setText("已寄送");
                                break;
                            case 1:
                                postStatus.setText("未寄送");
                                break;
                            case 3:
                                postStatus.setText("已接收");
                                break;
                            default:
                                break;
                        }
                    }
                    realName.setText(userAddr.getRealName());
                    mobile.setText(userAddr.getMobile());
                    address.setText(userAddr.getAreaName() + userAddr.getAddr());
                } else {
                    String a = String.valueOf(response.getErrors().get(0));
                    Toast.makeText(mContext, a.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Click
    void to_flow() {
        FlowActivity_.intent(mContext).extra("statusId", statusId).start();
    }

    @Click
    void btn_back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        CommonUtils.sendBroadCast(mContext, "com.yinduo.yongyou.orderupdata", null, null);
        OrderDetailActivity.this.setResult(BaseActivity.RESULT_ORDER);
        this.finish();
    }
}
