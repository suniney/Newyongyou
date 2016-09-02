package com.example.yanxu.newyongyou.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.adpter.GridViewAdapter;
import com.example.yanxu.newyongyou.callBack.CommonCallback;
import com.example.yanxu.newyongyou.entity.Common;
import com.example.yanxu.newyongyou.listener.OnDeleteClickedListener;
import com.example.yanxu.newyongyou.utils.CommonUtils;
import com.example.yanxu.newyongyou.utils.HTTPCons;
import com.example.yanxu.newyongyou.utils.MyOkHttpUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanxu 2016/4/1.
 * 上传打款凭条
 */
@EActivity(R.layout.upload_pic)
public class UpLoadPicActivity extends BaseActivity implements HTTPCons {
    @ViewById
    GridView grid_main;
    @ViewById
    RelativeLayout show_id;
    @ViewById
    Button btn_add;
    @ViewById
    TextView tv_add;
    @ViewById
    EditText pic_name;
    @ViewById
    EditText pic_num;
    @ViewById
    EditText pic_cardnum;
    @ViewById
    EditText pic_money;
    @ViewById
    EditText pic_remark;
    @Extra
    String orderId;

    GridViewAdapter adapter;
    Context mContext;
    private List<Map> data = new ArrayList<Map>();
    private List<String> photoList = new ArrayList<String>();
    Bitmap bitmap;
    String pics = "";
    String finalPic = "";

    @AfterViews
    void init() {
        mContext = UpLoadPicActivity.this;
        pic_money.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }


            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
        Map map = new HashMap();
        map.put("isShow", false);
        map.put("isCheck", true);
        map.put("text", "无图片");
        map.put("photo", "");
        data.add(map);
        adapter = new GridViewAdapter(mContext, data, new OnDeleteClickedListener() {
            @Override
            public void onDeleteClick(View v, String url, int position) {
                if (data.size() == 6) {
                    Map newmap = data.get(data.size() - 1);
                    String text = newmap.get("text").toString();

                    if (text == "有图片") {
                        Map map = new HashMap();
                        map.put("isShow", false);
                        map.put("isCheck", true);
                        map.put("text", "无图片");
                        map.put("photo", "");
                        data.add(map);
                    }
                }
                String delete = data.get(position).get("temp").toString();
                finalPic = finalPic.replace(delete + ",", "");

//                finalPic = remove(pics, delete + ",", 1);
                data.remove(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemClick(View v, String url, int position) {
                PicturePopWindow_.intent(mContext).startForResult(RESULT_SIGN_PHOTO);

            }
        });
        grid_main.setAdapter(adapter);

    }

    /**
     * @param s      要操作的字符串
     * @param string 要删除的字符
     * @param i      删除第几个
     * @return
     */
    public String remove(String s, String string, int i) {
        if (i == 1) {
            int j = s.indexOf(string);
            s = s.substring(0, j) + s.substring(j + 1);
            i--;
            return s;
        } else {
            int j = s.indexOf(string);
            i--;
            return s.substring(0, j + 1) + remove(s.substring(j + 1), string, i);
        }

    }

    @Click
    void btn_next() {
        String realName = pic_name.getText().toString().trim();
        String cardNumb = pic_num.getText().toString().trim();
        String bankNumb = pic_cardnum.getText().toString().trim();
        String remitvalue = pic_money.getText().toString().trim();
        String remark = pic_remark.getText().toString().trim();
        if (TextUtils.isEmpty(realName)) {
            Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardNumb)) {
            Toast.makeText(mContext, "身份证号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CommonUtils.verifyLength(cardNumb)) {
            Toast.makeText(mContext, "身份证号位数不正确", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(bankNumb)) {
            Toast.makeText(mContext, "银行卡号不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(remitvalue)) {
            Toast.makeText(mContext, "请输入金额", Toast.LENGTH_SHORT).show();
        } else if(!CommonUtils.isCorrectMoney(remitvalue)){
            Toast.makeText(mContext, "输入金额格式错误", Toast.LENGTH_SHORT).show();
        }else {
            upLoadPic(realName, cardNumb, bankNumb, remitvalue, remark);
        }
    }

    private void upLoadPic(String realName, String cardNumb, String bankNumb, String remitvalue, String remark) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("orderId", orderId);
        if ("".equals(finalPic)) {
            Toast.makeText(mContext, "请上传照片", Toast.LENGTH_SHORT).show();
        } else {
            map.put("pics", finalPic);
            map.put("realName", realName);
            map.put("cardNumb", cardNumb);
            map.put("bankNumb", bankNumb);
            map.put("remitvalue", remitvalue);
            if (!"".equals(remark)) {
                map.put("remark", remark);
            }
            MyOkHttpUtils.okhttpPost(this, uploadPaymentPic_action, map, new CommonCallback() {
                @Override
                public void onResponse(Common response,int id) {
                    Log.d("response", "isSucess" + response.toString());
                    boolean isSucess = response.isSuccess();
                    if (isSucess) {
                        CommonUtils.ToastShow(mContext, "上传成功");
                        UpLoadPicActivity.this.setResult(RESULT_ORDER);
                        finish();
                    }
                }

            });
        }

    }

    @Click
    void btn_back() {
        this.finish();
    }


    @OnActivityResult(RESULT_SIGN_PHOTO)
    void onResultPhoto(int resultCode, Intent intent) {
        if (resultCode == RESULT_SIGN_PHOTO) {
            if (data != null) {
//                int a = 0;
                bitmap = intent.getParcelableExtra("bitmap");
                String temp = intent.getStringExtra("temp");
                pics += temp + ",";
                System.out.print(pics);
                finalPic = pics;
                Map map = new HashMap();
                map.put("isShow", true);
                map.put("isCheck", false);
                map.put("photo", bitmap);
                map.put("temp", temp);
                map.put("text", "有图片");
                if (data.size() == 1) {
                    data.add(0, map);
                } else if (data.size() == 6) {
                    data.remove(data.size() - 1);
                    data.add(map);
                } else {
                    data.add(data.size() - 1, map);
                }
                adapter.notifyDataSetChanged();

            }

        }
    }

}
