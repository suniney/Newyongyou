package com.example.yanxu.newyongyou.entity;


import java.math.BigDecimal;

public class MyInfo {


    private String photoUrl; // 图片url      yy_photo表

    private String name; //名字

    private BigDecimal allIncome;  //累计收益 yy_user_info

    private String regMobile;

    public String getRegMobile() {
        return regMobile;
    }

    public void setRegMobile(String regMobile) {
        this.regMobile = regMobile;
    }
    //    private Integer isAuthentication; //身份认证(0,已认证  1，未认证)

//    private Integer isAddr;  //是否填写收货地址 (0,已填写，1未填写)

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public BigDecimal getAllIncome() {
        return allIncome;
    }

    public void setAllIncome(BigDecimal allIncome) {
        this.allIncome = allIncome;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
