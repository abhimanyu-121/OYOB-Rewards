package com.oyob.controller.model;

/**
 * Created by 121 on 9/7/2017.
 */

public class PointsModel {
    int pointsBalance;
    int allocatedPoints;
    int redeemedPoints;
    RecentRedeemModel recentRedeem;
    RecentAllocatedModel recentAllocated;

    public int getPointsBalance() {
        return pointsBalance;
    }

    public void setPointsBalance(int pointsBalance) {
        this.pointsBalance = pointsBalance;
    }

    public int getAllocatedPoints() {
        return allocatedPoints;
    }

    public void setAllocatedPoints(int allocatedPoints) {
        this.allocatedPoints = allocatedPoints;
    }

    public int getRedeemedPoints() {
        return redeemedPoints;
    }

    public void setRedeemedPoints(int redeemedPoints) {
        this.redeemedPoints = redeemedPoints;
    }

    public RecentRedeemModel getRecentRedeem() {
        return recentRedeem;
    }

    public void setRecentRedeem(RecentRedeemModel recentRedeem) {
        this.recentRedeem = recentRedeem;
    }

    public RecentAllocatedModel getRecentAllocated() {
        return recentAllocated;
    }

    public void setRecentAllocated(RecentAllocatedModel recentAllocated) {
        this.recentAllocated = recentAllocated;
    }
}
