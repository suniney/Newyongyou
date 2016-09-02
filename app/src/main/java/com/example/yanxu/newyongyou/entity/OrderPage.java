package com.example.yanxu.newyongyou.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/4/7.
 */
public class OrderPage {

    private int successOrders;

    private int allOrders;

    private int unpaidOrders;

    private List<MyOrder> MyOrder;

    public List<MyOrder> getMyOrder() {
        return MyOrder;
    }

    public void setMyOrder(List<MyOrder> myOrder) {
        MyOrder = myOrder;
    }

    public int getUnpaidOrders() {
        return unpaidOrders;
    }

    public void setUnpaidOrders(int unpaidOrders) {
        this.unpaidOrders = unpaidOrders;
    }

    public int getAllOrders() {
        return allOrders;
    }

    public void setAllOrders(int allOrders) {
        this.allOrders = allOrders;
    }

    public int getSuccessOrders() {
        return successOrders;
    }

    public void setSuccessOrders(int successOrders) {
        this.successOrders = successOrders;
    }



}
