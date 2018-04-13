package com.oyob.controller.model;

/**
 * Created by Narender Kumar on 2/26/2018.
 * For Prominent, Faridabad (India)
 * narender.kumar.nishad@gmail.com
 */

public class ModelCountries {

    private String countryId = "";
    private String countryName = "";

    public ModelCountries(String countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
