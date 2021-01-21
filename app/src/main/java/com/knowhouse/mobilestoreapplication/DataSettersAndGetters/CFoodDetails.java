package com.knowhouse.mobilestoreapplication.DataSettersAndGetters;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CFoodDetails {
    int foodDetailsId;
    double foodPrice;
    String foodSizes;
    int foodId;

    public CFoodDetails(int foodDetailsId, double foodPrice, String foodSizes,int foodId) {
        this.foodDetailsId = foodDetailsId;
        this.foodPrice = foodPrice;
        this.foodSizes = foodSizes;
        this.foodId = foodId;
    }

    public int getFoodDetailsId() {
        return foodDetailsId;
    }

    public BigDecimal getFoodPrice() {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(foodPrice));
        return bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
    }

    public String getFoodSizes() {
        return foodSizes;
    }

    public int getFoodId() {
        return foodId;
    }
}
