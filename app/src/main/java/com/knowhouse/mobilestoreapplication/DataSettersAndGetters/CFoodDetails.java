package com.knowhouse.mobilestoreapplication.DataSettersAndGetters;

class CFoodDetails {
    int foodDetailsId;
    String foodPrice;
    String foodSizes;

    public CFoodDetails(int foodDetailsId, String foodPrice, String foodSizes) {
        this.foodDetailsId = foodDetailsId;
        this.foodPrice = foodPrice;
        this.foodSizes = foodSizes;
    }

    public int getFoodDetailsId() {
        return foodDetailsId;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public String getFoodSizes() {
        return foodSizes;
    }
}
