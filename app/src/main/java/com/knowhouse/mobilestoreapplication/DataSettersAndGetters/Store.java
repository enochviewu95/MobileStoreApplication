package com.knowhouse.mobilestoreapplication.DataSettersAndGetters;

public class Store {
    private String storeName;
    private String storeLocation;

    public Store(String storeName, String storeLocation) {
        this.storeName = storeName;
        this.storeLocation = storeLocation;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreLocation() {
        return storeLocation;
    }
}
