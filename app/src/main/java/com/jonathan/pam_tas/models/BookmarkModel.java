package com.jonathan.pam_tas.models;

import java.io.Serializable;

public class BookmarkModel implements Serializable {
    String name;
    String description;
    Integer love;
    Integer sold;
    String type;
    String img_url;
    String price;

    public BookmarkModel(){

    }

    public BookmarkModel(String name, String description, Integer love, Integer sold, String type, String img_url) {
        this.name = name;
        this.description = description;
        this.love = love;
        this.sold = sold;
        this.type = type;
        this.img_url = img_url;
        this.price = price;
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

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
