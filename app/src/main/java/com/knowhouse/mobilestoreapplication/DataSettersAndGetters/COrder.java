package com.knowhouse.mobilestoreapplication.DataSettersAndGetters;

import java.io.Serializable;

public class COrder implements Serializable {
    CCustomer customer;
    CFoodCategory foodCategory;
    CFood food;
    CFoodDetails foodDetails;
    int orderId;

    public COrder(CCustomer customer, CFoodCategory foodCategory, CFood food, CFoodDetails foodDetails, int orderId) {
        this.customer = customer;
        this.foodCategory = foodCategory;
        this.food = food;
        this.foodDetails = foodDetails;
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public CCustomer getCustomer() {
        return customer;
    }

    public CFoodCategory getFoodCategory() {
        return foodCategory;
    }

    public CFood getFood() {
        return food;
    }

    public CFoodDetails getFoodDetails() {
        return foodDetails;
    }
}
