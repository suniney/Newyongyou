package com.example.yanxu.newyongyou.entity;


import java.math.BigDecimal;


public class HomeProductInfo {
    //产品id
    private Long id;
    //产品名称
    private String name;
    //类型 :  1、股票型 2、债券型 3、量化型 4、多策略',
    private Integer typeId;
    //期限(天数)
    private Integer cylcles;
    //产品形式
    private String productClas;
    //预期收益
    private BigDecimal interestRate;
    //起购金额
    private Integer startupMoney;
    //个人返佣
    private BigDecimal backInterest;
    private  String openDate;
    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getCylcles() {
        return cylcles;
    }

    public void setCylcles(Integer cylcles) {
        this.cylcles = cylcles;
    }

    public String getProductClas() {
        return productClas;
    }

    public void setProductClas(String productClas) {
        this.productClas = productClas;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getStartupMoney() {
        return startupMoney;
    }

    public void setStartupMoney(Integer startupMoney) {
        this.startupMoney = startupMoney;
    }

    public BigDecimal getBackInterest() {
        return backInterest;
    }

    public void setBackInterest(BigDecimal backInterest) {
        this.backInterest = backInterest;
    }
}
