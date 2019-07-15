package com.softices.traineeapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.softices.traineeapp.models.UserModel;
import com.softices.traineeapp.utils.Utility;

import java.util.ArrayList;

@SuppressLint("SdCardPath")
public class DatabaseManager extends SQLiteOpenHelper {
    private static String TAG = "DatabaseManager";
    private static SQLiteDatabase mDatabase;

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

    private String USER_TABLE = "CREATE TABLE "
            + TABLE_USER_TABLE + "("
            + TABLE_USER_COLUMN_ID + " INTEGER PRIMARY KEY,"
            + TABLE_USER_COLUMN_EMAIL + " TEXT,"
            + TABLE_USER_COLUMN_MOBILE + " TEXT,"
            + TABLE_USER_COLUMN_PASSWORD + " TEXT,"
            + TABLE_USER_COLUMN_FIRSTNAME + " TEXT,"
            + TABLE_USER_COLUMN_LASTNAME + " TEXT,"
            + TABLE_USER_COLUMN_USER_PHOTO + " blob)";


    public DatabaseManager(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        mDatabase = this.getWritableDatabase();
    }

    /**
     * \
     * Used to insert data while Sign Up.
     *
     * @param data
     */
    public void insertIntoTableUser(UserModel data) {
        try {
            ContentValues put = new ContentValues();
            put.put(TABLE_USER_COLUMN_FIRSTNAME, data.getFirstName());
            put.put(TABLE_USER_COLUMN_LASTNAME, data.getLastName());
            put.put(TABLE_USER_COLUMN_EMAIL, data.getEmail());
            put.put(TABLE_USER_COLUMN_MOBILE, data.getMobile());
            put.put(TABLE_USER_COLUMN_PASSWORD, data.getPassword());
            put.put(TABLE_USER_COLUMN_USER_PHOTO, Utility.getBytes(data.getPhoto()));
            mDatabase.insert(TABLE_USER_TABLE, null, put);
        } catch (Exception e) {
            Log.e(TAG, "insertIntoTableUser" + e);
        }
    }

    /**
     * \
     * Used to get all data in model using current login user mail.
     *
     * @param value
     * @return
     */
    public UserModel getUserDataByEmail(String value) {
        UserModel data = new UserModel();
        try {
            String query = "SELECT * FROM " + TABLE_USER_TABLE + " WHERE "
                    + TABLE_USER_COLUMN_EMAIL + "='" + value + "';";
            Cursor cursor = mDatabase.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                data.setFirstName(cursor.getString(cursor.getColumnIndex(TABLE_USER_COLUMN_FIRSTNAME)));
                data.setLastName(cursor.getString(cursor.getColumnIndex(TABLE_USER_COLUMN_LASTNAME)));
                data.setEmail(cursor.getString(cursor.getColumnIndex(TABLE_USER_COLUMN_EMAIL)));
                data.setMobile(cursor.getString(cursor.getColumnIndex(TABLE_USER_COLUMN_MOBILE)));
                data.setPassword(cursor.getString(cursor.getColumnIndex(TABLE_USER_COLUMN_PASSWORD)));
                data.setPhoto(Utility.getPhoto(cursor.getBlob(cursor.getColumnIndex(TABLE_USER_COLUMN_USER_PHOTO))));
                return data;
            }
        } catch (Exception e) {
            Log.e(TAG, "getUserDataByEmail " + e);
        }
        return data;
    }

    /**
     * \
     * Used to get list of all users registered.
     *
     * @param value
     * @return
     */
    public ArrayList<UserModel> getAllUserData(String value) {
        ArrayList<UserModel> userModelList = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + TABLE_USER_TABLE + " WHERE "
                    + TABLE_USER_COLUMN_EMAIL + "!='" + value + "';";
            Cursor cursor = mDatabase.rawQuery(query, null);

            while (cursor.moveToNext()) {
                UserModel data = new UserModel();
                String fName = cursor.getString(cursor.getColumnIndex(TABLE_USER_COLUMN_FIRSTNAME));
                String lName = cursor.getString(cursor.getColumnIndex(TABLE_USER_COLUMN_LASTNAME));
                String mail = cursor.getString(cursor.getColumnIndex(TABLE_USER_COLUMN_EMAIL));
                String mobile = cursor.getString(cursor.getColumnIndex(TABLE_USER_COLUMN_MOBILE));
                String password = cursor.getString(cursor.getColumnIndex(TABLE_USER_COLUMN_PASSWORD));
                data.setFirstName(fName);
                data.setEmail(mail);
                data.setLastName(lName);
                data.setMobile(mobile);
                data.setPassword(password);
                userModelList.add(data);
            }
            return userModelList;
        } catch (Exception e) {
            Log.e(TAG, "getAllUserData " + e);
        }
        return userModelList;
    }

    /**
     * \
     * Updates the detail of specific user.
     *
     * @param email
     * @param firstName
     * @param lastName
     * @param userPhoto
     */
    public void updateIntoTableUser(String email, String firstName,
                                    String lastName, String mobile, Bitmap userPhoto) {
        try {
            ContentValues put = new ContentValues();
            put.put(TABLE_USER_COLUMN_FIRSTNAME, firstName);
            put.put(TABLE_USER_COLUMN_LASTNAME, lastName);
            put.put(TABLE_USER_COLUMN_MOBILE, mobile);
            put.put(TABLE_USER_COLUMN_USER_PHOTO, Utility.getBytes(userPhoto));
            mDatabase.update(TABLE_USER_TABLE, put, "user_email='" + email + "'", null);
        } catch (Exception e) {
            Log.e(TAG, "updateIntoTableUser" + e);
        }
    }

    /**
     * \
     * This method checks the valid user while Login.
     *
     * @param mail
     * @param password
     * @return
     */
    public boolean isValidLogin(String mail, String password) {
        try {
            mDatabase = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_USER_TABLE + " WHERE "
                    + TABLE_USER_COLUMN_EMAIL + "='" + mail + "' AND "
                    + TABLE_USER_COLUMN_PASSWORD + "='" + password + "';";
            Cursor cursor = mDatabase.rawQuery(query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                mDatabase.close();
                return false;
            } else {
                cursor.close();
                mDatabase.close();
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "isValidLogin " + e);
        }
        return false;
    }

    /**
     * \
     * This method checks if email already exist in db.
     *
     * @param mail
     * @return
     */
    public boolean isEmailExist(String mail) {
        try {
            String[] columns = {
                    TABLE_USER_COLUMN_EMAIL
            };
            // selection criteria
            String selection = TABLE_USER_COLUMN_EMAIL + " = ?";

            String[] selectionArgs = {mail};

            Cursor cursor = mDatabase.query(TABLE_USER_TABLE, columns, selection,
                    selectionArgs, null, null, null);
            int Count = cursor.getCount();
            if (Count > 0) {
                return true;
            }

        } catch (Exception e) {
            Log.e(TAG, "isEmailExist " + e);
        }
        return false;
    }

    /**
     * \
     * This method is used to delete user.
     *
     * @param mail
     */

    public void deleteUser(String mail) {
        mDatabase.delete(TABLE_USER_TABLE, TABLE_USER_COLUMN_EMAIL + " = ?",
                new String[]{String.valueOf(mail)});
        mDatabase.close();
    }


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