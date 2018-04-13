package com.oyob.controller.model;
/**
 * Created by Trush on 08/05/16.
 */
public class UserDetailModel {
    String id;
    NameModel name;
    String gender;
    String dob;
    String email;
    int postCode;
    FacebookModel facebook;
    TwitterModel twitter;
    LoginRequestModel.Instagram instagram;
    MailModel mail;
    String imageKey;
    String status;
    boolean notifyRewardsAndPromotions;
    int allocatedPoints;
    int redeemedPoints;
    RecentRedeemModel recentRedeem;
    RecentAllocatedModel recentAllocated;
    String referralCode;
    String createdDate;
    String updatedDate;
    String facebookUrl;
    String twitterUrl;
    String instagramUrl;
    boolean setupCompleted;

    public NameModel getName() {
        return name;
    }

    public void setName(NameModel name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public FacebookModel getFacebook() {
        return facebook;
    }

    public void setFacebook(FacebookModel facebook) {
        this.facebook = facebook;
    }

    public TwitterModel getTwitter() {
        return twitter;
    }

    public void setTwitter(TwitterModel twitter) {
        this.twitter = twitter;
    }

    public LoginRequestModel.Instagram getInstagram() {
        return instagram;
    }

    public void setInstagram(LoginRequestModel.Instagram instagram) {
        this.instagram = instagram;
    }

    public MailModel getMail() {
        return mail;
    }

    public void setMail(MailModel mail) {
        this.mail = mail;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isNotifyRewardsAndPromotions() {
        return notifyRewardsAndPromotions;
    }

    public void setNotifyRewardsAndPromotions(boolean notifyRewardsAndPromotions) {
        this.notifyRewardsAndPromotions = notifyRewardsAndPromotions;
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

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public boolean isSetupCompleted() {
        return setupCompleted;
    }

    public void setSetupCompleted(boolean setupCompleted) {
        this.setupCompleted = setupCompleted;
    }
}
