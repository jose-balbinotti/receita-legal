package com.example.receitalegal.Model;

public class Recipe {

    String name;
    String description;
    String Ing1;
    String Ing2;
    String Ing3;
    String imgUrl;

    public Recipe(String name, String description, String ing1, String ing2, String ing3, String imgUrl) {
        this.name = name;
        this.description = description;
        Ing1 = ing1;
        Ing2 = ing2;
        Ing3 = ing3;
        this.imgUrl = imgUrl;
    }

    public Recipe(){

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

    public String getIng1() {
        return Ing1;
    }

    public void setIng1(String ing1) {
        Ing1 = ing1;
    }

    public String getIng2() {
        return Ing2;
    }

    public void setIng2(String ing2) {
        Ing2 = ing2;
    }

    public String getIng3() {
        return Ing3;
    }

    public void setIng3(String ing3) {
        Ing3 = ing3;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
