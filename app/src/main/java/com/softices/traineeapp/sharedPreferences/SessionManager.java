package com.softices.traineeapp.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Amit on 18/07/2018.
 */

public class SessionManager {
    SharedPreferences MyPref;
    SharedPreferences.Editor editor;
    Context context;
    int mode = 0;

    public static final String KEY_ID = "IsLoggedIn";
    public static final String U_token = "token";
    public static final String U_Id = "token_id";

    public SessionManager(Context context) {
        this.context = context;
        MyPref = context.getSharedPreferences("MyPref", mode);
        editor = MyPref.edit();
    }

    // getter & setter for webservice methods.
    public void createTokenSession(String token, Boolean isLogin) {
        editor.putBoolean(KEY_ID, isLogin);
        editor.putString(U_token, token);
        editor.commit();
    }

    public void createIdSession(String id, Boolean isLogin) {
        editor.putBoolean(KEY_ID, isLogin);
        editor.putString(U_Id, id);
        editor.commit();
    }

    public String getToken() {
        return MyPref.getString(U_token, null);
    }

    public String getId() {
        return MyPref.getString(U_Id, null);
    }
}
