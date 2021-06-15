package com.example.receitalegal;

public class Recipe {

    String name;
    String description;
    String img;
    String ingr1;
    String ingr2;
    String ingr3;

    public Recipe() {
    }

    public Recipe(String name, String description, String img, String ingr1, String ingr2, String ingr3) {
        this.name = name;
        this.description = description;
        this.img = img;
        this.ingr1 = ingr1;
        this.ingr2 = ingr2;
        this.ingr3 = ingr3;
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

    public String getIngr1() {
        return ingr1;
    }

    public void setIngr1(String ingr1) {
        this.ingr1 = ingr1;
    }

    public String getIngr2() {
        return ingr2;
    }

    public void setIngr2(String ingr2) {
        this.ingr2 = ingr2;
    }

    public String getIngr3() {
        return ingr3;
    }

    public void setIngr3(String ingr3) {
        this.ingr3 = ingr3;
    }
}
