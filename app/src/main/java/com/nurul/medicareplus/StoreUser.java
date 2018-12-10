package com.nurul.medicareplus;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.nurul.medicareplus.REG_MODEL.SignUp;

public class StoreUser {
    public final static String STORAGE= "com.nurul.medicare.user";

    public static SharedPreferences getPreference(Context context){
        return context.getSharedPreferences(STORAGE,Activity.MODE_PRIVATE);
    }

    public static void saveUserOnPreference(Context context, SignUp sign){
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString("name", sign.getName());
        editor.putString("email", sign.getEmail());
        editor.putString("userId", sign.getUserID());
        editor.putString("password", sign.getPassword());
        editor.putString("usertype", sign.getUserTYpe());
        editor.commit();
    }

    public static SignUp getUserOnPreference(Context context){
        SharedPreferences preferences = getPreference(context);
        String name = preferences.getString("name", null);
        String email = preferences.getString("email", null);
        String userid = preferences.getString("userId", null);
        String usertype = preferences.getString("usertype", null);
        String password = preferences.getString("password", null);
        return new SignUp(name,  email, password,  usertype,  userid);
    }

    public static void clearUser(Context context){
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.clear();
        editor.commit();
    }

}
