package com.example.mygrocerystore.models;
import java.io.Serializable;
public class NavCategoryDetailedModel implements Serializable {
    String name;
    String type;
    int price;
    String img_url;

    public NavCategoryDetailedModel() {
    }

    public NavCategoryDetailedModel(String name, String type, int price, String img_url) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.img_url = img_url;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
