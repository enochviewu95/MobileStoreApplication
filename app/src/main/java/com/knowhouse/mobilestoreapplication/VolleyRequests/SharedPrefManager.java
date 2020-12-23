package com.knowhouse.mobilestoreapplication.VolleyRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private static final String SHARE_PREF_NAME = "mysharedpref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_ID = "userid";
    private static final String KEY_LOCATION = "location";

    private SharedPrefManager (Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    public String getUserEmail(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL,null);
    }

    public String getUserFullName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FULL_NAME,null);
    }


    public String getPhoneNumber() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        return String.valueOf(sharedPreferences.getInt(KEY_PHONE_NUMBER, 0));
    }


    public String getUserID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID, null);
    }

    public String getLocation(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LOCATION,null);
    }

    public void setLocation(String myLocation){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LOCATION,myLocation);
        editor.apply();
    }

    public void onUserLogin(int id,int phoneNumber,String full_name,String email){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID,id);
        editor.putInt(KEY_PHONE_NUMBER,phoneNumber);
        editor.putString(KEY_FULL_NAME,full_name);
        editor.putString(KEY_EMAIL,email);
        editor.apply();
    }

    public boolean logOut(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
