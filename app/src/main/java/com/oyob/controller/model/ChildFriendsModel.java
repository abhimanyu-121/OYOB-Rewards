package com.oyob.controller.model;

/**
 * Created by 121 on 9/24/2017.
 */

public class ChildFriendsModel {
    public String android_image_url;
    public String child_image_url;
    public String name;
    public String text;
    public String Discount;
    public String Details;

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }




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
    public String getAndroid_image_url() {
        return android_image_url;
    }

    public void setAndroid_image_url(String android_image_url) {
        this.android_image_url = android_image_url;
    }
    public String getChild_image_url() {
        return child_image_url;
    }

    public void setChild_image_url(String child_image_url) {
        this.child_image_url = child_image_url;
    }
}