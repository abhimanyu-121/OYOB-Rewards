package com.oyob.controller.model;

/**
 * Created by 121 on 9/20/2017.
 */

public class ConnectModel {

    public String Tag;
    public String Details;

    public boolean isPressed;
    public String name;
    public String Discount;
    public String getRedeemUrl() {
        return redeemUrl;
    }
    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }
    public void setRedeemUrl(String redeemUrl) {
        this.redeemUrl = redeemUrl;
    }

    public String redeemUrl;
    public String text;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String productId;
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFav_bg() {
        return fav_bg;
    }

    public void setFav_bg(String fav_bg) {
        this.fav_bg = fav_bg;
    }

    public String fav_bg;

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }



    public String android_image_url;

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
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



}
