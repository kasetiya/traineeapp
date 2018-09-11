package com.softices.traineeapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softices.traineeapp.R;
import com.softices.traineeapp.database.DatabaseManager;
import com.softices.traineeapp.models.UserModel;
import com.softices.traineeapp.sharedPreferences.AppPref;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edtMail;
    private Button btnFetch;
    private String mail;
    private DatabaseManager dbManager;
    private UserModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

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
        model = new UserModel();

        edtMail = (EditText) findViewById(R.id.edt_mail);

        btnFetch = (Button) findViewById(R.id.btn_fetch);
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = edtMail.getText().toString();
                model = dbManager.getUserDataByEmail(mail);
                Toast.makeText(ForgotPasswordActivity.this, "Password is: " + model.getPassword(),
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ForgotPasswordActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });


    }

    /** \
     * This method functions when clicked on toolbar items
     * @param menuItem
     * @return
     */
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
