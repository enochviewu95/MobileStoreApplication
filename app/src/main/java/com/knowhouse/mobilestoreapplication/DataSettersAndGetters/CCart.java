package com.knowhouse.mobilestoreapplication.DataSettersAndGetters;

public class CCart {
    int id;
    String foodImageUrl;
    String foodName;
    String foodDescription;
    String foodQuantity;
    String foodOption;
    String foodPrice;

    public CCart(int id, String foodImageUrl, String foodName, String foodDescription,
                 String foodQuantity, String foodOption, String foodPrice) {
        this.id = id;
        this.foodImageUrl = foodImageUrl;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodQuantity = foodQuantity;
        this.foodOption = foodOption;
        this.foodPrice = foodPrice;
    }

    public int getId() {
        return id;
    }

    public String getFoodImageUrl() {
        return foodImageUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public String getFoodQuantity() {
        return foodQuantity;
    }

    public String getFoodOption() {
        return foodOption;
    }

    public String getFoodPrice() {
        return foodPrice;
    }
}
