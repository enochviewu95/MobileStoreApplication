package com.knowhouse.mobilestoreapplication.DataSettersAndGetters;


import java.io.Serializable;

public class CFood implements Serializable {
    private int foodId;
    private String foodName;
    private String foodImageUrl;
    private int foodRating;
    private String foodDescription;
    private int foodCategoryID;

    public CFood(int foodId, String foodName, String foodImageUrl, int foodRating, String foodDescription, int foodCategoryID) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodImageUrl = foodImageUrl;
        this.foodRating = foodRating;
        this.foodDescription = foodDescription;
        this.foodCategoryID = foodCategoryID;
    }

    public int getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodImageUrl() {
        return foodImageUrl;
    }

    public int getFoodRating() {
        return foodRating;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public int getFoodCategoryID() {
        return foodCategoryID;
    }
}
