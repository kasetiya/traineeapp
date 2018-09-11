package com.softices.traineeapp.sharedPreferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.softices.traineeapp.activities.SigninActivity;

public class AppPref {
    private static SharedPreferences sp;

    public static void setUserEmail(Context context, String value) {
        sp = context.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", value);
        editor.apply();
    }

    public static String getUserEmail(Context context) {
        sp = context.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        return sp.getString("email", "");
    }

    public static void setIsUserLogin(Context context, Boolean value) {
        sp = context.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("is_login", value);
        editor.apply();
    }

    public static Boolean getIsUserLogin(Context context) {
        sp = context.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        return sp.getBoolean("is_login", false);
    }

    public static void clearAppPref(Context context) {
        sp = context.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, SigninActivity.class);
        context.startActivity(intent);
    }
}