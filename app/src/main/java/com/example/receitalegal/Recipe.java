package com.example.receitalegal;

import android.net.Uri;

import java.util.ArrayList;

public class Recipe {

    String name;
    String description;
    String img;


    public Recipe() {
    }

    public Recipe(String name, String description, String img) {
        this.name = name;
        this.description = description;
//    this.img = Uri.parse(img);
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
