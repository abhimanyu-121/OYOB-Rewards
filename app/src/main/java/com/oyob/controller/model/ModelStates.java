package com.oyob.controller.model;

/**
 * Created by Narender Kumar on 2/26/2018.
 * For Prominent, Faridabad (India)
 * narender.kumar.nishad@gmail.com
 */

public class ModelStates {

    private String stateCode = "";
    private String stateName = "";
    private String countryCode = "";

    public ModelStates(String stateCode, String stateName, String countryCode) {
        this.stateCode = stateCode;
        this.stateName = stateName;
        this.countryCode = countryCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
