package com.knowhouse.mobilestoreapplication.VolleyRequests;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private static final String SHARE_PREF_NAME = "mysharedpref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ID = "userid";

    private SharedPrefManager (Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void onLogin(int id,String email){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID,id);
        editor.putString(KEY_EMAIL,email);

        editor.apply();
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    public boolean logOut(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getUserEmail(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL,null);
    }
}
