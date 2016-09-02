package com.example.yanxu.newyongyou.utils;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * @author yanxu
 * @date 2016/4/26.
 */
public class BankUtils {
    public static Bitmap getBankIcon(String bankName, Context context) {
        Bitmap bankIcon = null;
        if ("中国银行".equals(bankName)) {
            bankIcon = CommonUtils.getImageFromAssetsFile("1.png", context);
        } else if ("中信银行".equals(bankName)) {
            bankIcon = CommonUtils.getImageFromAssetsFile("2.png", context);
        } else if ("中国光大银行".equals(bankName)) {
            bankIcon = CommonUtils.getImageFromAssetsFile("3.png", context);
        } else if ("交通银行".equals(bankName)) {
            bankIcon = CommonUtils.getImageFromAssetsFile("4.png", context);
        } else if ("平安银行".equals(bankName)) {
            bankIcon = CommonUtils.getImageFromAssetsFile("5.png", context);
        } else if ("浦发银行".equals(bankName)) {
            bankIcon = CommonUtils.getImageFromAssetsFile("6.png", context);
        } else if ("中国民生银行".equals(bankName)) {
            bankIcon = CommonUtils.getImageFromAssetsFile("7.png", context);
        } else if ("招商银行".equals(bankName)) {
            bankIcon = CommonUtils.getImageFromAssetsFile("8.png", context);
        } else if ("中国建设银行".equals(bankName)) {
            bankIcon = CommonUtils.getImageFromAssetsFile("9.png", context);
        } else if ("中国工商银行".equals(bankName)) {
            bankIcon = CommonUtils.getImageFromAssetsFile("10.png", context);
        } else if ("中国农业银行".equals(bankName)) {
            bankIcon = CommonUtils.getImageFromAssetsFile("11.png", context);
        } else if ("兴业银行".equals(bankName)) {
            bankIcon = CommonUtils.getImageFromAssetsFile("12.png", context);
        }
        return bankIcon;
    }

}
