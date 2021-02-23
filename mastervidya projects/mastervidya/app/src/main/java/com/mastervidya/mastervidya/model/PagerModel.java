package com.mastervidya.mastervidya.model;

public class PagerModel
{
    private String description;
    private String title;
    private int imgId;

    public PagerModel( String title,String description, int imgId) {
        this.description = description;
        this.title = title;
        this.imgId = imgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}

