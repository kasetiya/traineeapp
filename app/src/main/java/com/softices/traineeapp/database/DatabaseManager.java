package com.softices.traineeapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;

import com.softices.traineeapp.activities.SigninActivity;
import com.softices.traineeapp.models.UserModel;
import com.softices.traineeapp.utils.Utility;

import java.util.ArrayList;

@SuppressLint("SdCardPath")
public class DatabaseManager {
    private static String TAG = "DatabaseManager";
    private DatabaseHelper mHelper;
    private static SQLiteDatabase mDatabase;


    public DatabaseManager(Context context) {
        mHelper = new DatabaseHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }


    public void deleteTable(String tablename) {
        mDatabase.delete(tablename, null, null);
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
            put.put(DatabaseHelper.TABLE_USER_COLUMN_FIRSTNAME, data.getFirstName());
            put.put(DatabaseHelper.TABLE_USER_COLUMN_LASTNAME, data.getLastName());
            put.put(DatabaseHelper.TABLE_USER_COLUMN_EMAIL, data.getEmail());
            put.put(DatabaseHelper.TABLE_USER_COLUMN_MOBILE, data.getMobile());
            put.put(DatabaseHelper.TABLE_USER_COLUMN_PASSWORD, data.getPassword());
            put.put(DatabaseHelper.TABLE_USER_COLUMN_USER_PHOTO, Utility.getBytes(data.getPhoto()));
            mDatabase.insert(DatabaseHelper.TABLE_USER_TABLE, null, put);
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
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_USER_TABLE + " WHERE "
                    + DatabaseHelper.TABLE_USER_COLUMN_EMAIL + "='" + value + "';";
            Cursor cursor = mDatabase.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                data.setFirstName(cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_FIRSTNAME)));
                data.setLastName(cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_LASTNAME)));
                data.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_EMAIL)));
                data.setMobile(cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_MOBILE)));
                data.setPassword(cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_PASSWORD)));
                data.setPhoto(Utility.getPhoto(cursor.getBlob(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_USER_PHOTO))));
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
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_USER_TABLE + " WHERE "
                    + DatabaseHelper.TABLE_USER_COLUMN_EMAIL + "!='" + value + "';";
            Cursor cursor = mDatabase.rawQuery(query, null);

            while (cursor.moveToNext()) {
                UserModel data = new UserModel();
                String fName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_USER_COLUMN_FIRSTNAME));
                String lName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_USER_COLUMN_LASTNAME));
                String mail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_USER_COLUMN_EMAIL));
                String mobile = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_USER_COLUMN_MOBILE));
                String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_USER_COLUMN_PASSWORD));
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
     * Used to change password.
     *
     * @param email
     * @param pass
     */
    public void updatePassword(String email, String pass) {
        try {
            ContentValues put = new ContentValues();
            put.put(DatabaseHelper.TABLE_USER_COLUMN_PASSWORD, pass);
            mDatabase.update(DatabaseHelper.TABLE_USER_TABLE, put, "user_email='" + email + "'", null);
        } catch (Exception e) {
            Log.e(TAG, "updatePassword" + e);
        }
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
            put.put(DatabaseHelper.TABLE_USER_COLUMN_FIRSTNAME, firstName);
            put.put(DatabaseHelper.TABLE_USER_COLUMN_LASTNAME, lastName);
            put.put(DatabaseHelper.TABLE_USER_COLUMN_MOBILE, mobile);
            put.put(DatabaseHelper.TABLE_USER_COLUMN_USER_PHOTO, Utility.getBytes(userPhoto));
            mDatabase.update(DatabaseHelper.TABLE_USER_TABLE, put, "user_email='" + email + "'", null);
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
            mDatabase = mHelper.getReadableDatabase();
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_USER_TABLE + " WHERE "
                    + DatabaseHelper.TABLE_USER_COLUMN_EMAIL + "='" + mail + "' AND "
                    + DatabaseHelper.TABLE_USER_COLUMN_PASSWORD + "='" + password + "';";
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
                    DatabaseHelper.TABLE_USER_COLUMN_EMAIL
            };
            // selection criteria
            String selection = DatabaseHelper.TABLE_USER_COLUMN_EMAIL + " = ?";

            String[] selectionArgs = {mail};

            Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_USER_TABLE, columns, selection,
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
        mDatabase.delete(DatabaseHelper.TABLE_USER_TABLE, DatabaseHelper.TABLE_USER_COLUMN_EMAIL + " = ?",
                new String[]{String.valueOf(mail)});
        mDatabase.close();
    }


}