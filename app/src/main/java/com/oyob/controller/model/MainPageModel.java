package com.oyob.controller.model;

/**
 * Created by 121 on 9/18/2017.
 */

public class MainPageModel {
    public String name;
    private int id;
    private String ext;
    private String new_cat_image;

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNew_cat_image() {
        return new_cat_image;
    }

    public void setNew_cat_image(String new_cat_image) {
        this.new_cat_image = new_cat_image;
    }
}
