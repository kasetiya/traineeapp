package com.softices.traineeapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.softices.traineeapp.R;
import com.softices.traineeapp.sharedPreferences.AppPref;

public class SplashActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        callSigninActivity();
    }

    /**
     * Redirect to SignIn activity after 3000 ms,
     * if user is already logged in then redirect to Dashboard activity.
     */
    private void callSigninActivity() {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (AppPref.getIsUserLogin(SplashActivity.this)) {
                        intent = new Intent(SplashActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        intent = new Intent(SplashActivity.this, SigninActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timerThread.start();
    }
}
