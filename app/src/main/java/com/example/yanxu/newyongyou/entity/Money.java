package com.example.yanxu.newyongyou.entity;


import java.math.BigDecimal;

public class Money{


    private BigDecimal allIncome;  //累计收益 yy_user_info

    private BigDecimal balance;  //余额 ，可提现资金


    public BigDecimal getAllIncome() {
        return allIncome;
    }

    public void setAllIncome(BigDecimal allIncome) {
        this.allIncome = allIncome;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
