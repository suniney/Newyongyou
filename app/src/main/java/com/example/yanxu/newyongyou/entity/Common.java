package com.example.yanxu.newyongyou.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanxu 2016/4/1.
 */
//实体类
public class Common {


    private List errors;
    private boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    public List getErrors() {
        return errors;
    }

    public void setErrors(ArrayList errors) {
        this.errors = errors;
    }


    @Override
    public String toString() {
        return this.isSuccess + " " + errors;
    }
}
