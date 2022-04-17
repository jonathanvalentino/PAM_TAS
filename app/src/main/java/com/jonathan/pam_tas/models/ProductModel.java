package com.jonathan.pam_tas.models;

public class ProductModel {
    String name;
    String description;
    String love;
    String sold;
    String type;
    String img_url;

    public ProductModel(){

    }

    public ProductModel(String name, String description, String love, String sold, String type, String img_url) {
        this.name = name;
        this.description = description;
        this.love = love;
        this.sold = sold;
        this.type = type;
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
