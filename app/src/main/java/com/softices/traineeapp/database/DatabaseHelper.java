package com.softices.traineeapp.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressLint("SdCardPath")
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "TraineeApp";
    private static final int DATABASE_VERSION = 1;

    //-------------table User
    public static final String TABLE_USER_TABLE = "user_table";
    public static final String TABLE_USER_COLUMN_ID = "user_id";
    public static final String TABLE_USER_COLUMN_FIRSTNAME = "user_first_name";
    public static final String TABLE_USER_COLUMN_LASTNAME = "user_last_name";
    public static final String TABLE_USER_COLUMN_EMAIL = "user_email";
    public static final String TABLE_USER_COLUMN_MOBILE = "user_mobile";
    public static final String TABLE_USER_COLUMN_PASSWORD = "user_password";
    public static final String TABLE_USER_COLUMN_USER_PHOTO = "user_photo";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    private String USER_TABLE = "CREATE TABLE "
            + TABLE_USER_TABLE + "("
            + TABLE_USER_COLUMN_ID + " INTEGER PRIMARY KEY,"
            + TABLE_USER_COLUMN_EMAIL + " TEXT,"
            + TABLE_USER_COLUMN_MOBILE + " TEXT,"
            + TABLE_USER_COLUMN_PASSWORD + " TEXT,"
            + TABLE_USER_COLUMN_FIRSTNAME + " TEXT,"
            + TABLE_USER_COLUMN_LASTNAME + " TEXT,"
            + TABLE_USER_COLUMN_USER_PHOTO + " blob)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(USER_TABLE);
        } catch (Exception e) {
            Log.e("onCreate", "" + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("drop table if exists " + TABLE_USER_TABLE);
            onCreate(db);
        } catch (Exception e) {
            Log.e("onUpgrade", "" + e);
        }
    }
}