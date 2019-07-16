package com.softices.traineeapp.activities;

import android.Manifest;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.softices.traineeapp.R;
import com.softices.traineeapp.database.DatabaseManager;
import com.softices.traineeapp.dialogsAndValidations.L;
import com.softices.traineeapp.models.UserModel;
import com.softices.traineeapp.sharedPreferences.AppPref;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    private EditText edtFirstName, edtLastName, edtEmail, edtMobile;
    private ImageView ivPhoto;
    private Button btnChange;
    private Uri selectedImage;
    private DatabaseManager dbManager;
    private UserModel userModel;
    private String TAG = "ProfileActivity", currentmail;
    private final int PICK_IMAGE_CAMERA = 0, PICK_IMAGE_GALLERY = 1, REQUEST_CODE = 2;

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
        dbManager = new DatabaseManager(this);
        userModel = new UserModel();
        currentmail = AppPref.getUserEmail(this);
        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtEmail = findViewById(R.id.edt_email);
        edtMobile = findViewById(R.id.edt_mobile);
        ivPhoto = findViewById(R.id.iv_photo);
        ivPhoto.setOnClickListener(view -> onOpenImage());
        btnChange = findViewById(R.id.btn_change);
        btnChange.setOnClickListener(view -> onSaveChange());

        setDefaultData();
    }

    /**
     * \
     * Set default data of current login user.
     */
    private void setDefaultData() {
        userModel = dbManager.getUserDataByEmail(currentmail);
        ivPhoto.setImageBitmap(userModel.getPhoto());
        edtFirstName.setText(userModel.getFirstName());
        edtLastName.setText(userModel.getLastName());
        edtEmail.setText(userModel.getEmail());
        edtEmail.setEnabled(false);
        edtMobile.setText(userModel.getMobile());
    }

    /**
     * \
     * Checks runtime permission for API > 23.
     */
    public void onOpenImage() {
        if (Build.VERSION.SDK_INT < 23) {
            selectImage();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission
                    .CAMERA) == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest
                        .permission.CAMERA, Manifest
                        .permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }

    /**
     * \
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            switch (requestCode) {
                case REQUEST_CODE:
                    goWithCameraPermission(grantResults);
                    break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * \
     * Request for Camera permission.
     *
     * @param grantResults
     */
    private void goWithCameraPermission(int[] grantResults) {
        if (grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else if (grantResults[1] == PackageManager.PERMISSION_DENIED ||
                grantResults[0] == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest
                    .permission.WRITE_EXTERNAL_STORAGE, Manifest
                    .permission.CAMERA}, REQUEST_CODE);
        }
    }

    /**
     * Opens PickImageDialog to choose,
     * Camera or Gallery
     */
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                dialog.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                selectedImage = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                        "image_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
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
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
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

    /**
     * saving image as bitmap in imageview.
     *
     * @param bitmap
     */
    private void setImageData(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                ivPhoto.setImageBitmap(decoded);
            } else {
                L.t(this, "Unable to select image");
            }
        } catch (Exception e) {
            Log.e(TAG, "setImageData" + e);
        }
    }


    /**
     * Update changes to db.
     */
    private void onSaveChange() {
        String mFirstName = edtFirstName.getText().toString();
        String mLastName = edtLastName.getText().toString();
        String mMobile = edtMobile.getText().toString();
        Bitmap imageBtimap = ((BitmapDrawable) ivPhoto.getDrawable()).getBitmap();
        try {
            if (L.validName(mFirstName)) {
                Toast.makeText(this, getString(R.string.txt_enter_first_name), Toast.LENGTH_SHORT).show();
            } else if (L.validName(mLastName)) {
                Toast.makeText(this, getString(R.string.txt_enter_last_name), Toast.LENGTH_SHORT).show();
            } else if (L.validMobile(mMobile)) {
                Toast.makeText(this, getString(R.string.txt_enter_valid_number), Toast.LENGTH_SHORT).show();
            } else {
                userModel.setFirstName(mFirstName);
                userModel.setLastName(mLastName);
                userModel.setMobile(mMobile);
                userModel.setPhoto(imageBtimap);

                dbManager.updateIntoTableUser(currentmail, mFirstName, mLastName, mMobile, imageBtimap);
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "onSaveChange" + e);
        }
    }

    /**
     * This method functions when clicked on toolbar item.
     *
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
