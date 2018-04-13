package com.oyob.controller.model;

/**
 * Created by Narender Kumar on 2/26/2018.
 * For Prominent, Faridabad (India)
 * narender.kumar.nishad@gmail.com
 */

public class ModelInterests {

    private String interest_id ="";
    private String interest_name = "";

    public ModelInterests(String interest_id, String interest_name) {
        this.interest_id = interest_id;
        this.interest_name = interest_name;
    }

    public String getInterest_id() {
        return interest_id;
    }

    public void setInterest_id(String interest_id) {
        this.interest_id = interest_id;
    }

    public String getInterest_name() {
        return interest_name;
    }

    public void setInterest_name(String interest_name) {
        this.interest_name = interest_name;
    }
}