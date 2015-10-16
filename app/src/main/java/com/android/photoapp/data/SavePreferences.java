package com.android.photoapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SavePreferences {

    private static SharedPreferences shared_preferences;

    public SavePreferences(Context context) {
        shared_preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setIsLoggedIn(boolean flag) {
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.putBoolean("isLogged", flag);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return shared_preferences.getBoolean("isLogged", false);
    }

    public void setUserPhoneNumber(String userId) {
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.putString("person_mobile", userId);
        editor.commit();
    }

    public String getUserPhoneNumber() {
        String userId = "";
        userId = shared_preferences.getString("person_mobile", "0");
        return userId;
    }


    public void setUserName(String userName) {
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.putString("person_name", userName);
        editor.commit();
    }

    public String getUserName() {
        String userName = "";
        userName = shared_preferences.getString("person_name", "");
        return userName;
    }

    public void clearPrefs() {
        shared_preferences.edit().clear().commit();
    }
}
