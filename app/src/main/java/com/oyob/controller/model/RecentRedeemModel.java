package com.oyob.controller.model;

/**
 * Created by Trush on 15/05/16.
 */
public class RecentRedeemModel {
    String rewardName;
    String rewardId;
    int points;
    String redeemedOn;
    String brandId;
    String brandName;


    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getRedeemedOn() {
        return redeemedOn;
    }

    public void setRedeemedOn(String redeemedOn) {
        this.redeemedOn = redeemedOn;
    }
}
