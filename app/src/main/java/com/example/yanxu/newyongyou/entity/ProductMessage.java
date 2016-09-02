package com.example.yanxu.newyongyou.entity;

/**
 * Created by Administrator on 2016/4/6.
 */

public class ProductMessage {

    private String pmTitle;   //产品公告标题
    private Integer pmDate; //产品公告发布日期
    private String pmContent;   //产品公告内容
    private Integer created;
    private String id;
    private String pmDesc;

    public String getPmDesc() {
        return pmDesc;
    }

    public void setPmDesc(String pmDesc) {
        this.pmDesc = pmDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPmTitle() {
        return pmTitle;
    }

    public void setPmTitle(String pmTitle) {
        this.pmTitle = pmTitle;
    }

    public Integer getPmDate() {
        return pmDate;
    }

    public void setPmDate(Integer pmDate) {
        this.pmDate = pmDate;
    }

    public String getPmContent() {
        return pmContent;
    }

    public void setPmContent(String pmContent) {
        this.pmContent = pmContent;
    }


    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }


}
