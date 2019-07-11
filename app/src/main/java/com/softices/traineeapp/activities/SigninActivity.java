package com.softices.traineeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.softices.traineeapp.R;
import com.softices.traineeapp.database.DatabaseManager;
import com.softices.traineeapp.dialogsAndValidations.L;
import com.softices.traineeapp.sharedPreferences.AppPref;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView tvSignup;
    private Intent i;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        init();
    }

    /**
     * Initializing all views.
     */
    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        dbManager = new DatabaseManager(this);

        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtEmail.setText("softices@gmail.com");
        edtPassword.setText("12345678");

        tvSignup = (TextView) findViewById(R.id.tv_link_signup);
        tvSignup.setOnClickListener(this);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                doSIgnIn();
                break;
            case R.id.tv_link_signup:
                i = new Intent(this, SignupActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    /**
     * \
     * Successfully log in user after checking
     * all validation.
     */
    private void doSIgnIn() {
        String password = edtPassword.getText().toString();
        String email = edtEmail.getText().toString();
        if (L.validEmail(email)) {
            Toast.makeText(this, getString(R.string.txt_enter_valid_mail), Toast.LENGTH_SHORT).show();
        } else if (L.validPassword(password)) {
            Toast.makeText(this, getString(R.string.txt_valid_password), Toast.LENGTH_SHORT).show();
        } else {
            Boolean valid = dbManager.isValidLogin(email, password);
            if (valid) {
                i = new Intent(this, DashboardActivity.class);
                startActivity(i);

                AppPref.setUserEmail(SigninActivity.this, edtEmail.getText().toString());
                AppPref.setIsUserLogin(SigninActivity.this, true);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_invalid_login), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
