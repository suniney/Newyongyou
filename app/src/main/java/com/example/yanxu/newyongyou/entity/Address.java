package com.example.yanxu.newyongyou.entity;

/**
 * @author yanxu
 * @date 2016/4/27.
 */
public class Address {
    private Long userId;
    private String addr; //详细地址信息
    private Integer isDef;   //是否为默认
    private String areaName;  //省市区县地址
    private String realName;  //真实姓名
    private String mobile;  //省市区县地址
    private String postStatus;  //邮寄状态
    private String id;  //
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getIsDef() {
        return isDef;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setIsDef(Integer isDef) {
        this.isDef = isDef;
    }
}
