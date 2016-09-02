package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.widget.Button;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.utils.HTTPCons;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by yanxu 2016/4/1.
 * 订单流程
 */
@EActivity(R.layout.flow)
public class FlowActivity extends BaseActivity implements HTTPCons {
    @ViewById
    Button btn_1;
    @ViewById
    Button btn_2;
    @ViewById
    Button btn_3;
    @ViewById
    Button btn_4;
    @ViewById
    Button btn_5;
    @ViewById
    Button btn_6;
    @ViewById
    Button btn_7;
    @ViewById
    Button btn_8;
    @ViewById
    Button btn_9;
    @Extra
    int statusId;
    Context mContext;

    @AfterViews
    void init() {
        setTranStatusBar();
        mContext = FlowActivity.this;
        switch (statusId) {
            case STATUS_BOOK_CHECK:
//                status.setText("预约审核中");
                break;
            case STATUS_BOOK_UNSUCCESS:
//                status.setText("预约失败");
                break;
            case STATUS_REUPLOAD_SIGN:
                btn_1.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_2.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_3.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_4.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_5.setTextColor(getResources().getColor(R.color.text_yellow));
                break;
            case STATUS_OVER:
                btn_1.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_2.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_3.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_4.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_5.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_6.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_7.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_8.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_9.setTextColor(getResources().getColor(R.color.text_yellow));
                break;
            case STATUS_BOOK_SUCCESS:
                btn_1.setTextColor(getResources().getColor(R.color.text_yellow));
                break;

            case STATUS_UPLOAD_CHECK:
                btn_1.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_2.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_3.setTextColor(getResources().getColor(R.color.text_yellow));
                break;
            case STATUS_UPLOAD_MONEY:
                btn_1.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_2.setTextColor(getResources().getColor(R.color.text_yellow));
                break;
            case STATUS_FINISH:
                btn_1.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_2.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_3.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_4.setTextColor(getResources().getColor(R.color.text_yellow));
                break;
            case STATUS_UPLOAD_SIGN:
                btn_1.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_2.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_3.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_4.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_5.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_6.setTextColor(getResources().getColor(R.color.text_yellow));
                break;
            case STATUS_BACK_MONEY:
                btn_1.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_2.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_3.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_4.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_5.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_6.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_7.setTextColor(getResources().getColor(R.color.text_yellow));
                break;
            case STATUS_RECYCLE_SIGN:
                btn_1.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_2.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_3.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_4.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_5.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_6.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_7.setTextColor(getResources().getColor(R.color.text_yellow));
                btn_8.setTextColor(getResources().getColor(R.color.text_yellow));
                break;
            case STATUS_CLOSE:
//                btn_9.setTextColor(getResources().getColor(R.color.text_yellow));
                break;
            default:
                break;
        }
    }


    @Click
    void btn_back() {
        this.finish();
    }
}
