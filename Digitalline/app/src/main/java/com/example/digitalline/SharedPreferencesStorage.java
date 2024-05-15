package com.example.digitalline;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesStorage {
    private static final String databaseName = "SharedPrefs";
    public static final String isManagerPreference = "isManager";


    public static void saveBoolean(String preferenceName, boolean value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferenceName, value);
        editor.apply();
    }

    public static void saveString(String preferenceName, String value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, value);
        editor.apply();
    }

    public static void saveInt(String preferenceName, int value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(preferenceName, value);
        editor.apply();
    }

    public static void saveLong(String preferenceName, long value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(preferenceName, value);
        editor.apply();
    }

    public static void saveFloat(String preferenceName, float value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(preferenceName, value);
        editor.apply();
    }

    public static boolean getBoolean(String preferenceName, boolean defaultValue, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(preferenceName, defaultValue);
    }

    public static String getString(String preferenceName, String defaultValue, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    public static int getInt(String preferenceName, int defaultValue, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(preferenceName, defaultValue);
    }

    public static long getLong(String preferenceName, long defaultValue, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(preferenceName, defaultValue);
    }

    public static float getFloat(String preferenceName, float defaultValue, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(preferenceName, defaultValue);
    }

    public static void clearDatabase(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
