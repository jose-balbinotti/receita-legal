package com.example.receitalegal;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Ingredient implements Parcelable {
    public String unitType;
    public String ingredient;
    public String quantity;

    public Ingredient() {

    }

    public Ingredient(String unitType, String ingredient, String quantity) {
        this.unitType = unitType;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }


    protected Ingredient(Parcel in) {
        unitType = in.readString();
        ingredient = in.readString();
        quantity = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(unitType);
        parcel.writeString(ingredient);
        parcel.writeString(quantity);
    }
}
