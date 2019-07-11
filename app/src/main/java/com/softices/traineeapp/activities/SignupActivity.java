package com.softices.traineeapp.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.softices.traineeapp.R;
import com.softices.traineeapp.database.DatabaseManager;
import com.softices.traineeapp.dialogsAndValidations.L;
import com.softices.traineeapp.models.UserModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SignupActivity extends AppCompatActivity {
    private EditText edtFirstName, edtLastName, edtEmail, edtMobile, edtPassword, edtConPassword;
    private ImageView ivPhoto;
    private DatabaseManager dbManager;
    private Uri selectedImage;
    private Bitmap photoBitmap;
    private String TAG = "SignupActivity";
    private final int PICK_IMAGE_CAMERA = 0, PICK_IMAGE_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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

        edtFirstName = (EditText) findViewById(R.id.edt_first_name);
        edtLastName = (EditText) findViewById(R.id.edt_last_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtMobile = (EditText) findViewById(R.id.edt_mobile);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConPassword = (EditText) findViewById(R.id.edt_conf_password);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);

        edtFirstName.setText("Softices");
        edtLastName.setText("softices");
        edtEmail.setText("softices@gmail.com");
        edtMobile.setText("1234567890");
        edtPassword.setText("12345678");
        edtConPassword.setText("12345678");
    }

    /**
     * \
     * Checks runtime permission for API > 23.
     *
     * @param view
     */
    public void onClickSelectPhoto(View view) {
        if (Build.VERSION.SDK_INT < 23) {
            selectImage();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission
                    .CAMERA) == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                ActivityCompat.requestPermissions(SignupActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            switch (requestCode) {
                case 1:
                    goWithCameraPermission(grantResults);
                    break;
                default:
                    break;
            }
        }
    }

    private void goWithCameraPermission(int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SignupActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    /**
     * Opens PickImageDialog to choose,
     * Camera or Gallery
     */
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    dialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    selectedImage = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "image_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, selectedImage);
                    startActivityForResult(intent, PICK_IMAGE_CAMERA);
                } else if (options[item].equals("Choose From Gallery")) {
                    dialog.dismiss();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(pickPhoto, "Compelete action using"),
                            PICK_IMAGE_GALLERY);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PICK_IMAGE_CAMERA:
                try {
                    if (selectedImage != null) {
                        Bitmap photoBitmap = BitmapFactory.decodeFile(selectedImage.getPath());
                        setImageData(photoBitmap);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "PICK_FROM_CAMERA" + e);
                }
                break;

            case PICK_IMAGE_GALLERY:
                try {
                    if (resultCode == RESULT_OK) {
                        Uri uri = imageReturnedIntent.getData();
                        if (uri != null) {
                            Bitmap bitmap = null;
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            setImageData(bitmap);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "PICK_FROM_GALLERY" + e);
                }
                break;
        }
    }

    private void setImageData(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                Glide.with(this)
                        .load(out.toByteArray())
                        .into(ivPhoto);
            } else {
                L.t(this, "Unable to select image");
            }
        } catch (Exception e) {
            Log.e(TAG, "setImageData" + e);
        }
    }

    /**
     * Users Sign up here.
     *
     * @param view
     */
    public void onClickDoSignup(View view) {
        String mFirstName = edtFirstName.getText().toString();
        String mLastName = edtLastName.getText().toString();
        String mEmail = edtEmail.getText().toString();
        String mMobile = edtMobile.getText().toString();
        String mPassword = edtPassword.getText().toString();
        String mConPassword = edtConPassword.getText().toString();
        photoBitmap = ((BitmapDrawable) ivPhoto.getDrawable()).getBitmap();
        try {
            if (L.validName(mFirstName)) {
                Toast.makeText(this, getString(R.string.txt_enter_first_name), Toast.LENGTH_SHORT).show();
                edtFirstName.setFocusable(true);
            } else if (L.validName(mLastName)) {
                Toast.makeText(this, getString(R.string.txt_enter_last_name), Toast.LENGTH_SHORT).show();
                edtLastName.setFocusable(true);
            } else if (L.validEmail(mEmail)) {
                Toast.makeText(this, getString(R.string.txt_enter_valid_mail), Toast.LENGTH_SHORT).show();
                edtEmail.setFocusable(true);
            } else if (L.validMobile(mMobile)) {
                Toast.makeText(this, getString(R.string.txt_enter_valid_number), Toast.LENGTH_SHORT).show();
                edtMobile.setFocusable(true);
            } else if (L.validPassword(mPassword)) {
                Toast.makeText(this, getString(R.string.txt_valid_password), Toast.LENGTH_SHORT).show();
                edtPassword.setFocusable(true);
            } else if (L.validPassword(mConPassword)) {
                Toast.makeText(this, getString(R.string.txt_valid_password), Toast.LENGTH_SHORT).show();
                edtConPassword.setFocusable(true);
            } else if (!L.isPasswordMatch(mPassword, mConPassword)) {
                Toast.makeText(this, getString(R.string.txt_password_mismatch), Toast.LENGTH_SHORT).show();
                edtConPassword.setFocusable(true);
            } else if (!dbManager.isEmailExist(mEmail)){
                UserModel userModel = new UserModel();
                userModel.setFirstName(mFirstName);
                userModel.setLastName(mLastName);
                userModel.setEmail(mEmail);
                userModel.setMobile(mMobile);
                userModel.setPassword(mPassword);
                userModel.setPhoto(photoBitmap);

                dbManager.insertIntoTableUser(userModel);
                Toast.makeText(this, getString(R.string.txt_success_sign_up), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();

            }
        } catch (Exception e) {
            Log.e(TAG, "onClickDoSignup" + e);
        }
    }

    public void onClear(View view) {
        edtFirstName.setText("");
        edtLastName.setText("");
        edtEmail.setText("");
        edtMobile.setText("");
        edtPassword.setText("");
        edtConPassword.setText("");
    }

    /**
     * User navigate to Sign In.
     *
     * @param view
     */
    public void onClickLinkSignin(View view) {
        startActivity(new Intent(this, SigninActivity.class));
        finish();
    }

    /**
     * \
     * This method functions when clicked on toolbar items
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}