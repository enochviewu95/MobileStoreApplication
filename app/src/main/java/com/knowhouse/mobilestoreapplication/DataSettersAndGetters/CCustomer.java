package com.knowhouse.mobilestoreapplication.DataSettersAndGetters;

public class CCustomer extends CUsers {
    int age;
    String customerUserImageUrl;


    public CCustomer(String name, String email, String phoneNumber, String location,
                     String userId, int age, String customerUserImageUrl) {
        super(name, email, phoneNumber, location, userId);
        this.age = age;
        this.customerUserImageUrl = customerUserImageUrl;
    }

    public int getAge() {
        return age;
    }

    public String getCustomerUserImageUrl() {
        return customerUserImageUrl;
    }
}
