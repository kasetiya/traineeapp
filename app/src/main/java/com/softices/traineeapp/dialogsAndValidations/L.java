package com.softices.traineeapp.dialogsAndValidations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.softices.traineeapp.R;

public class L {

    public static String TAG = "L";
    private static ProgressDialog progressDialog;

    //---------is connected to network
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    //-----------------alert for error and finish activity
    public static void displayAlertAndFinish(final Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                ((Activity) context).finish();
                            }
                        }).show();
    }

    //-----------------alert for error and finish activity
    public static void displayAlert(final Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
    }

    public static void t(Context context, String msg) {
        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNotNull(String setText) {
        return setText != null && !setText.equals("null") && !setText.equals("") && !setText.isEmpty();
    }

    //-----------------show progress dailog
    public static void showProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }


    //-----------------hide loading dialog box
    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    //-----------------username validation
    public static boolean isValidName(String editText)
            throws NumberFormatException {
        if (editText.length() == 0 || editText.trim().equals("")) {
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String editText) {
        if (editText.length() == 0) {
            return false;
        } else {
            String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
            String emailInput = editText.trim();
            return emailInput.matches(EMAIL_PATTERN);
        }
    }

    public static boolean isValidMobile(String editText) throws NumberFormatException {
        if (editText.length() == 0 && editText.contains(" ") && editText.length() < 10) {
            return false;
        } else {
            String postCodePattrn = "[0-9]{4,14}";
            String postCodeInput = editText.trim();
            return postCodeInput.matches(postCodePattrn);
        }
    }

    //-----------------password validation
    public static boolean isValidPassword(String editText)
            throws NumberFormatException {
        if (editText.length() == 0 || editText.trim().equals("")) {
            return false;
        } else if (editText.length() <= 5 || editText.length() > 16) {
            return false;
        } else {
            return true;
        }
    }

    //-----------------match password and confirm password
    public static boolean isPasswordMatch(String editPassword, String editTextConfirmPassword)
            throws NumberFormatException {
        if (editPassword.equals(editTextConfirmPassword)) {
            return true;
        } else {
            return false;
        }
    }

    //-----------------moibile number validation
    public static boolean isValidMobile(EditText editText) throws NumberFormatException {
        if (editText.getText().length() == 0 && editText.getText().toString().contains(" ")) {
            return false;
        } else {
            String postCodePattrn = "[0-9]{4,14}";
            String postCodeInput = editText.getText().toString().trim();
            if (postCodeInput.matches(postCodePattrn)) {
                return true;
            } else {
                return false;
            }
        }
    }
}