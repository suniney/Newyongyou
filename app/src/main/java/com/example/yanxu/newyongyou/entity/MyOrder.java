package com.example.yanxu.newyongyou.entity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/4/6.
 */
public class MyOrder {

    private Long orderId; //订单id
    private Long proId;

    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    private Integer orderDate;  //订单日期

    private BigDecimal money;  //订单金额

    private String productName;  //产品名称

    public String getAddrId() {
        return addrId;
    }

    public void setAddrId(String addrId) {
        this.addrId = addrId;
    }

    private String addrId;
    private Integer status;  //订单状态：0预约失败  1.预约中审核  2.预约成功 3.重新上传打款凭证 4打款凭证审核 5.客户完成交易
    // 6重新上传合同  7 上传合同签署页  8首次返佣  9回收合同  10订单完成 11 订单关闭',


    public Integer getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Integer orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
