package com.example.yanxu.newyongyou.entity;


public class UserCenter {


    private String headUrl;
    private Integer isIdentity;  //身份认证  0已认证  1未认证

    private Integer isPostAddr;  //收货地址 0已填写 1未填写

    private Integer isBank; //是否绑定银行卡

    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;
    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public Integer getIsBank() {
        return isBank;
    }

    public void setIsBank(Integer isBank) {
        this.isBank = isBank;
    }

    public Integer getIsIdentity() {
        return isIdentity;
    }

    public void setIsIdentity(Integer isIdentity) {
        this.isIdentity = isIdentity;
    }

    public Integer getIsPostAddr() {
        return isPostAddr;
    }

    public void setIsPostAddr(Integer isPostAddr) {
        this.isPostAddr = isPostAddr;
    }
}
