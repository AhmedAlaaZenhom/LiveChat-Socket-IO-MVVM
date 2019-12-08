package com.intcore.internship.livechat.data.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {

    private static final String LOCALE = "LOCALE";
    public static final String LOCALE_ARABIC = "ar";
    public static final String LOCALE_ENGLISH = "en";

    private static final String LOGGED_IN = "LOGGED_IN";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_API_TOKEN = "USER_NAME";

    private static final String TAG = PreferenceHelper.class.getSimpleName();

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public PreferenceHelper(Context applicationContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        editor = preferences.edit();
    }

    private void save(String var0, String var1) {
        editor.putString(var0, var1);
        editor.commit();
    }

    private String get(String var0) {
        return preferences.getString(var0, "");
    }

    private Boolean containsKey(String var0) {
        return preferences.contains(var0);
    }

    private void removeKey(String var0) {
        editor.remove(var0);
        editor.commit();
    }


    public void setUserLoggedIn(){
        save(LOGGED_IN, String.valueOf(true)); ;
    }

    public boolean isUserLoggedIn(){
        return containsKey(LOGGED_IN);
    }

    public void saveUserName(String userName) {
        save(USER_NAME, userName);
    }

    public String getUserName(){
        return get(USER_NAME) ;
    }

    public String getUserApiToken(){
        return get(USER_API_TOKEN) ;
    }
    public String getSavedLocale(){
        return get(LOCALE) ;
    }


}
