package com.example.yanxu.newyongyou.entity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/4/7.
 */
public class OrderDetailPage {


    private String orderNumb;  //订单号

    private Integer orderDate;  //订单日期

    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    private Long proId;

    private String proName;  //产品名称

    private String realRame;  //真实姓名

    private BigDecimal money;  //订单金额

    private String phoneNumb;  //联系电话

    private String remark;  //备注信息

    private Integer status;  //订单状态：1预约失败  2.预约中审核  3.预约成功 4.重新上传打款凭证 5打款凭证审核 6.客户完成交易

    // 7重新上传合同  8 上传合同签署页  9首次返佣  10回收合同  11订单完成 12订单关闭',
    private UserAddr userAddrs;

    public Integer getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(Integer postStatus) {
        this.postStatus = postStatus;
    }

    private  Integer postStatus;  //邮寄状态

    public UserAddr getUserAddrs() {
        return userAddrs;
    }

    public void setUserAddrs(UserAddr userAddrs) {
        this.userAddrs = userAddrs;
    }

    public String getOrderNumb() {
        return orderNumb;
    }

    public void setOrderNumb(String orderNumb) {
        this.orderNumb = orderNumb;
    }

    public Integer getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Integer orderDate) {
        this.orderDate = orderDate;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getRealRame() {
        return realRame;
    }

    public void setRealRame(String realRame) {
        this.realRame = realRame;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getPhoneNumb() {
        return phoneNumb;
    }

    public void setPhoneNumb(String phoneNumb) {
        this.phoneNumb = phoneNumb;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
