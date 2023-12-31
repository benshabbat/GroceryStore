package com.example.mygrocerystore.models;

import java.io.Serializable;

public class ViewAllModel implements Serializable {
    String name;
    String type;
    String img_url;
    int price;

    public ViewAllModel() {
    }

    public ViewAllModel(String name, String type, String img_url, int price) {
        this.name = name;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
