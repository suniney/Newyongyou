package com.example.yanxu.newyongyou.listener;

public interface OrderClickedListener {

    void onCancelClick(String id);

    void onSignClick(String id);

    void onMoneyClick(String id);
    void onOrderClick(String proiId, String orderId, String addrId);
    void onAddress(String id);
    void onLayoutClick(String orderId, String addrId);

}