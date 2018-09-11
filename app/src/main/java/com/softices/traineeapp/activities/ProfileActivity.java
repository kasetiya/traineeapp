package com.softices.traineeapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

        tvMail = (TextView) findViewById(R.id.tv_mail_value);
        tvName = (TextView) findViewById(R.id.tv_name_value);
        tvMobile = (TextView) findViewById(R.id.tv_mobile_value);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);


        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
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
