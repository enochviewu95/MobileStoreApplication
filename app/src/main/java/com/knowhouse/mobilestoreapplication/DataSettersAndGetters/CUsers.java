package com.knowhouse.mobilestoreapplication.DataSettersAndGetters;

import java.io.Serializable;

public class CUsers implements Serializable {
    String name;
    String email;
    String phoneNumber;
    String location;
    String userId;

    public CUsers(String name, String email, String phoneNumber, String location, String userId) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLocation() {
        return location;
    }
}
