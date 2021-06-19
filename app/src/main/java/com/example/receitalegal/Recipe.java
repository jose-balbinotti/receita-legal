package com.example.receitalegal;


import java.util.ArrayList;
import java.util.List;

public class Recipe {

    String name;
    String description;
    String img;
    ArrayList<List> ingredients;

    public Recipe() {
    }

    public Recipe(String name, String description, String img, ArrayList<List> ingredients) {
        this.name = name;
        this.description = description;
        this.img = img;
        this.ingredients = ingredients;
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

    public ArrayList<List> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<List> ingredients) {
        this.ingredients = ingredients;
    }
}
