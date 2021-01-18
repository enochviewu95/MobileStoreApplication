package com.knowhouse.mobilestoreapplication.DataSettersAndGetters;

import java.io.Serializable;

public class CFoodCategory implements Serializable {

    private int foodCategoryId;
    private String categoryName;
    private String categoryImageUrl;

    public CFoodCategory(int foodCategoryId, String categoryName, String categoryImageUrl) {
        this.foodCategoryId = foodCategoryId;
        this.categoryName = categoryName;
        this.categoryImageUrl = categoryImageUrl;
    }

    public int getFoodCategoryId() {
        return foodCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }
}
