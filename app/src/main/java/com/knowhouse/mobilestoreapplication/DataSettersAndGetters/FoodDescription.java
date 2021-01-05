package com.knowhouse.mobilestoreapplication.DataSettersAndGetters;

public class FoodDescription {
    private String foodImageUrl;
    private int foodRating;
    private String foodName;
    private int foodPrice;
    private int storeID;
    private int foodID;

    public FoodDescription(int foodID,String foodImageUrl,int foodRating,
                           String foodName,int storeID,int foodPrice) {
        this.foodImageUrl = foodImageUrl;
        this.foodRating = foodRating;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
    }

    public String getFoodImageUrl() {
        return foodImageUrl;
    }

    public int getFoodRating() {
        return foodRating;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public int getStoreID() {
        return storeID;
    }

    public int getFoodID() {
        return foodID;
    }
}
