package com.softices.traineeapp.dialogsAndValidations;

import android.content.Context;
import android.widget.Toast;

public class L {

    public static String TAG = "L";

    public static void t(Context context, String msg) {
        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
    }

    //-----------------username validation
    public static boolean validName(String editText) {
        return editText.length() == 0 || editText.trim().equals("");
    }

    public static boolean validEmail(String editText) {
        if (editText.length() == 0) {
            return true;
        } else {
            String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
            String emailInput = editText.trim();
            return !emailInput.matches(EMAIL_PATTERN);
        }
    }

    public static boolean validMobile(String editText) throws NumberFormatException {
        editText.length();
        String postCodePattrn = "[0-9]{4,14}";
        String postCodeInput = editText.trim();
        return !postCodeInput.matches(postCodePattrn);
    }

    //-----------------password validation
    public static boolean validPassword(String editText)
            throws NumberFormatException {
        if (editText.length() == 0 || editText.trim().equals("")) {
            return true;
        } else return editText.length() <= 5 || editText.length() > 16;
    }

    //-----------------match password and confirm password
    public static boolean isPasswordMatch(String editPassword, String editTextConfirmPassword) {
        return editPassword.equals(editTextConfirmPassword);
    }
}