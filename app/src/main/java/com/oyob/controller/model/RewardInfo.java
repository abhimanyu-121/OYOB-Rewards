package com.oyob.controller.model;

/**
 * Created by Trush on 08/04/16.
 */
public class RewardInfo {
    String id;
    String productId;

    String rewardType;
    String rewardName;
    String label;
    int speedDial;
    float price;
    int allocated;
    int redeemed;
    int redemptionLimit;
    int allocatedCnt;
    int redeemedCnt;
    float credit;
    String desc;
    String subDesc;
    String termsAndConditions;
    String tags;
    String status;
    String imageKey;
    String createdDate;
    String updatedDate;

    private boolean isRedeemInStore;
    private boolean isInStoreReward;
    private boolean isWalkedIn;
    private String rewardSubType;
    private String promoCode;
    private String earnPoints;
    private Offer offerDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getSpeedDial() {
        return speedDial;
    }

    public void setSpeedDial(int speedDial) {
        this.speedDial = speedDial;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAllocated() {
        return allocated;
    }

    public void setAllocated(int allocated) {
        this.allocated = allocated;
    }

    public int getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(int redeemed) {
        this.redeemed = redeemed;
    }

    public int getRedemptionLimit() {
        return redemptionLimit;
    }

    public void setRedemptionLimit(int redemptionLimit) {
        this.redemptionLimit = redemptionLimit;
    }

    public int getAllocatedCnt() {
        return allocatedCnt;
    }

    public void setAllocatedCnt(int allocatedCnt) {
        this.allocatedCnt = allocatedCnt;
    }

    public int getRedeemedCnt() {
        return redeemedCnt;
    }

    public void setRedeemedCnt(int redeemedCnt) {
        this.redeemedCnt = redeemedCnt;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public String getSubDesc() {
        return subDesc;
    }

    public void setSubDesc(String subDesc) {
        this.subDesc = subDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
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

/*    public PostBrandModel getBrand() {
        return brand;
    }

    public void setBrand(PostBrandModel brand) {
        this.brand = brand;
    }*/

    public boolean isRedeemInStore() {
        return isRedeemInStore;
    }

    public void setRedeemInStore(boolean redeemInStore) {
        isRedeemInStore = redeemInStore;
    }

    public boolean isInStoreReward() {
        return isInStoreReward;
    }

    public void setInStoreReward(boolean inStoreReward) {
        isInStoreReward = inStoreReward;
    }

    public boolean isWalkedIn() {
        return isWalkedIn;
    }

    public void setWalkedIn(boolean walkedIn) {
        isWalkedIn = walkedIn;
    }

    public String getRewardSubType() {
        return rewardSubType;
    }

    public void setRewardSubType(String rewardSubType) {
        this.rewardSubType = rewardSubType;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getEarnPoints() {
        return earnPoints;
    }

    public void setEarnPoints(String earnPoints) {
        this.earnPoints = earnPoints;
    }

    public Offer getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(Offer offerDetails) {
        this.offerDetails = offerDetails;
    }

    /*public class RewardBrandModel{
        String id;
        String businessName;
        LocationModel location;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public LocationModel getLocation() {
            return location;
        }

        public void setLocation(LocationModel location) {
            this.location = location;
        }
    }*/
}
