package com.knowhouse.mobilestoreapplication.DataSettersAndGetters;

import android.os.Parcel;
import android.os.Parcelable;

public class CCart implements Parcelable {
    int id;
    String foodImageUrl;
    String foodName;
    String foodDescription;
    String foodQuantity;
    String foodOption;
    String foodPrice;
    String identity;

    public CCart(int id, String foodImageUrl, String foodName, String foodDescription,
                 String foodQuantity, String foodOption, String foodPrice) {
        this.id = id;
        this.foodImageUrl = foodImageUrl;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodQuantity = foodQuantity;
        this.foodOption = foodOption;
        this.foodPrice = foodPrice;
        this.identity = String.valueOf(id);
    }

    public CCart(Parcel in){
        String[] data  = new String[7];
        in.readStringArray(data);
        this.identity = data[0];
        this.foodImageUrl = data[1];
        this.foodName = data[2];
        this.foodDescription = data[3];
        this.foodQuantity = data[4];
        this.foodOption = data[5];
        this.foodPrice = data[6];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] data = new String[]{
                this.identity,
                this.foodImageUrl,
                this.foodName,
                this.foodDescription,
                this.foodQuantity,
                this.foodOption,
                this.foodPrice};
        dest.writeStringArray(data);
    }

    public static final Parcelable.Creator<CCart> CREATOR = new Creator<CCart>() {
        @Override
        public CCart createFromParcel(Parcel source) {
            return new CCart(source);
        }

        @Override
        public CCart[] newArray(int size) {
            return new CCart[size];
        }
    };

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
