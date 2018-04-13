package com.oyob.controller.model;

/**
 * Created by 121 on 9/15/2017.
 */

public class SearchModel {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    String name;
    String Discount;
}