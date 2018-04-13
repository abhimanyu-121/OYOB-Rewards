package com.oyob.controller.model;

/**
 * Created by 121 on 9/24/2017.
 */

public class FriendsModel {
    public boolean isPresssed;


    public boolean isPresssed() {
        return isPresssed;
    }

    public void setPresssed(boolean presssed) {
        isPresssed = presssed;
    }

    public String android_image_url;

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String Tag;

    public String name;
    public String Discount;
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
}
