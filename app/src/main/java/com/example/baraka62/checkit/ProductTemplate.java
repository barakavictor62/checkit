package com.example.baraka62.checkit;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by baraka62 on 11/25/2017.
 */
@IgnoreExtraProperties
public class ProductTemplate {
    private String name,description,price,imgUrl,id;
    Bitmap img;
    public ProductTemplate(){

    }

    public ProductTemplate(String name,String description, String price,String imgUrl){
        this.name = name;
        this.id = id;
        this.price = price;
        this.imgUrl = imgUrl;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
