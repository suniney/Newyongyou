package com.example.yanxu.newyongyou.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/4/7.
 */
public class HomePage  {

    private List<HomeBanner> homeBanners;

    private List<String> productType;

    private List<HomeProductInfo> homeProductInfos;
    private String  headUrl;
    private String  messageStatus;

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public List<HomeBanner> getHomeBanners() {
        return homeBanners;
    }

    public void setHomeBanners(List<HomeBanner> homeBanners) {
        this.homeBanners = homeBanners;
    }

    public List<String> getProductType() {
        return productType;
    }

    public void setProductType(List<String> productType) {
        this.productType = productType;
    }

    public List<HomeProductInfo> getHomeProductInfos() {
        return homeProductInfos;
    }

    public void setHomeProductInfos(List<HomeProductInfo> homeProductInfos) {
        this.homeProductInfos = homeProductInfos;
    }
}
