package com.softices.traineeapp.application;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    public static MyApplication sInstance;
    private static String tag = "traineeapp";
    private RequestQueue mRequestQueue;
    
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            MultiDex.install(this);
            sInstance = this;
        } catch (Exception e) {
            Log.e(tag + "error", e + "");
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        FirebaseApp.initializeApp(this);
    }

    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void add(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? tag : tag);
        getRequestQueue().add(req);
    }

    public <T> void add(Request<T> req) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public MyApplication() {
        super();
    }
}