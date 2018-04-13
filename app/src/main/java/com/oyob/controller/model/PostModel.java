package com.oyob.controller.model;

import java.util.List;

/**
 * Created by deepak on 21-02-2016.
 */
public class PostModel {
    String id;
    String brandId;
    String postMessage;
    String imageKey;
    String status;
    String createdOn;
    PostBrandModel brand;
    boolean isAlreadyReportAbused;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public PostBrandModel getBrand() {
        return brand;
    }

    public void setBrand(PostBrandModel brand) {
        this.brand = brand;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public boolean isAlreadyReportAbused() {
        return isAlreadyReportAbused;
    }

    public void setAlreadyReportAbused(boolean alreadyReportAbused) {
        isAlreadyReportAbused = alreadyReportAbused;
    }

    public static class PostBrandModel {
        String id;
        boolean isFollow;
        String businessName;
        /*LocationModel location;*/
        List<RewardCountModel> rewardCount;
        String[] images;

        public String[] getImages() {
            return images;
        }

        public void setImages(String[] images) {
            this.images = images;
        }

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

       /* public LocationModel getLocation() {
            return location;
        }

        public void setLocation(LocationModel location) {
            this.location = location;
        }
*/

        public boolean isFollow() {
            return isFollow;
        }

        public void setFollow(boolean follow) {
            isFollow = follow;
        }

        public List<RewardCountModel> getRewardCount() {
            return rewardCount;
        }

        public void setRewardCount(List<RewardCountModel> rewardCount) {
            this.rewardCount = rewardCount;
        }

    }

    public class RewardCountModel{
        String type;
        int count;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}



