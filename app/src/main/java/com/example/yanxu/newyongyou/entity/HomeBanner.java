package com.example.yanxu.newyongyou.entity;


public class HomeBanner {

    private String actUrl; //活动页面地址     yy_activity表

    private String photoUrl; // 图片url      yy_photo表

    public String getActUrl() {
        return actUrl;
    }

    public void setActUrl(String actUrl) {
        this.actUrl = actUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
