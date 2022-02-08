package com.sapir.nanittest.db;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {
    private static final String SHARED_PREFS_KEY = "com.sapir.nanittest.sharedpreference.SHARED_PREFS_KEY";


    public enum AppKey {
        BABY_USER
    }

    private static Storage instance;
    private static SharedPreferences sharedPrefs;

    public static Storage getInstance() {
        if (instance == null || sharedPrefs == null) {
            instance = new Storage();
        }

        return instance;
    }

    public void initialize(Context context) {
        if (context == null) {
            return;
        }

        sharedPrefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
    }


    public void putString(AppKey key, String value) {
        sharedPrefs.edit().putString(key.toString(), value).apply();
    }

    public String getString(AppKey key, String defaultValue) {
        return sharedPrefs.getString(key.toString(), defaultValue);
    }

}
