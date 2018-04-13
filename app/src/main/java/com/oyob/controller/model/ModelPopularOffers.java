package com.oyob.controller.model;

/**
 * Created by Narender Kumar on 2/23/2018.
 * Form Faridabad (India)
 * narender.kumar.nishad@gmail.com
 */

public class ModelPopularOffers {
    private String offerId = "";
    private String clientId = "";
    private String imageLink = "";
    private String image = "";
    private String type = "";

    public ModelPopularOffers() {
    }

    public ModelPopularOffers(String offerId, String clientId, String imageLink, String image, String type) {
        this.offerId = offerId;
        this.clientId = clientId;
        this.imageLink = imageLink;
        this.image = image;
        this.type = type;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
