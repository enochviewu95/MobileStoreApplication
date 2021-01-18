package com.knowhouse.mobilestoreapplication.DataSettersAndGetters;


import java.io.Serializable;

public class CFood implements Serializable {
    private int foodId;
    private String foodName;
    private String foodImageUrl;
    private int foodRating;
    private String foodDescription;
    private CFoodDetails foodDetails;

    public CFood(int foodId, String foodName, String foodImageUrl, int foodRating, String foodDescription, CFoodDetails foodDetails) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodImageUrl = foodImageUrl;
        this.foodRating = foodRating;
        this.foodDescription = foodDescription;
        this.foodDetails = foodDetails;
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

    public CFoodDetails getFoodDetails() {
        return foodDetails;
    }
}
