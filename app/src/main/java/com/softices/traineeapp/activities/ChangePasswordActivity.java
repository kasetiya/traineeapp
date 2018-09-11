package com.softices.traineeapp.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.softices.traineeapp.R;
import com.softices.traineeapp.database.DatabaseManager;
import com.softices.traineeapp.dialogsAndValidations.L;
import com.softices.traineeapp.models.UserModel;
import com.softices.traineeapp.sharedPreferences.AppPref;

import java.io.InputStream;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtPassword, edtConfPass;
    private TextView tvPassword;
    private Button btnSave;
    private DatabaseManager dbManager;
    private UserModel userModel;
    private String TAG = "ChangePasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

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
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConfPass = (EditText) findViewById(R.id.edt_conf_password);
        tvPassword = (TextView) findViewById(R.id.tv_password);
        userModel = dbManager.getUserDataByEmail(AppPref.getUserEmail(ChangePasswordActivity.this));
        tvPassword.setText(userModel.getPassword());
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
    }

    /** \
     * This method invokes when any view is clicked.
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                onSaveChanges();
        }
    }

    /** \
     * Update the entered password in db.
     */
    private void onSaveChanges() {
        String mPassword = edtPassword.getText().toString();
        String mConPassword = edtConfPass.getText().toString();
        try {
            if (!L.isValidPassword(mPassword)) {
                Toast.makeText(this, getString(R.string.txt_valid_password), Toast.LENGTH_SHORT).show();
            } else if (!L.isValidPassword(mConPassword)) {
                Toast.makeText(this, getString(R.string.txt_valid_password), Toast.LENGTH_SHORT).show();
            } else if (!L.isPasswordMatch(mPassword, mConPassword)) {
                Toast.makeText(this, getString(R.string.txt_password_mismatch), Toast.LENGTH_SHORT).show();
            } else {
                userModel.setPassword(mPassword);
                dbManager.updatePassword(AppPref.getUserEmail(this), mPassword);
                finish();
            }
        } catch (Exception e)

        {
            Log.e(TAG, "onClickDoSignup" + e);
        }
    }

    /** \
     * This method functions when clicked on toolbar items
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
