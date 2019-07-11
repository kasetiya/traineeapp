package com.softices.traineeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.softices.traineeapp.R;
import com.softices.traineeapp.database.DatabaseManager;
import com.softices.traineeapp.models.UserModel;
import com.softices.traineeapp.sharedPreferences.AppPref;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvName, tvMail, tvMobile;
    private ImageView ivPhoto;
    private Button btnUpdate;
    private String currentMail;
    private DatabaseManager dbManager;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        userModel = new UserModel();

        tvMail = findViewById(R.id.tv_mail_value);
        tvName = findViewById(R.id.tv_name_value);
        tvMobile = findViewById(R.id.tv_mobile_value);
        ivPhoto = findViewById(R.id.iv_photo);


        btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
            finish();
        });

        setDefaultData();

    }

    /** \
     * Set default data of current login user.
     */
    private void setDefaultData() {
        currentMail = AppPref.getUserEmail(ProfileActivity.this);
        userModel = dbManager.getUserDataByEmail(currentMail);
        ivPhoto.setImageBitmap(userModel.getPhoto());
        tvMail.setText(userModel.getEmail());
        tvMobile.setText(userModel.getMobile());
        tvName.setText(userModel.getFirstName());

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
