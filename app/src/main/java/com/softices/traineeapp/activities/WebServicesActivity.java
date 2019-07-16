package com.softices.traineeapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.softices.traineeapp.R;
import com.softices.traineeapp.application.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebServicesActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGet, btnPost, btnPatch, btnDelete;
    private String TAG = "WebServicesActivity", jsonResponse, tagJsonObj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_services);

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

        btnGet = findViewById(R.id.btn_get);
        btnGet.setOnClickListener(this);
        btnPost = findViewById(R.id.btn_post);
        btnPost.setOnClickListener(this);
        btnPatch = findViewById(R.id.btn_patch);
        btnPatch.setOnClickListener(this);
        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
    }

    /**
     * \
     * Invokes when any view is clicked.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                getResponse();
                break;
            case R.id.btn_post:
                postService();
                break;
            case R.id.btn_patch:
                putRequest();
                break;
            case R.id.btn_delete:
                deleteRequest();
                break;
        }
    }

    /**
     * \
     * This method is for DeleteService.
     */
    private void deleteRequest() {
        String url = "http://httpbin.org/delete";
        StringRequest deleteReq = new StringRequest(Request.Method.DELETE,
                url, response -> {
            Log.e(TAG, "" + response);
            Toast.makeText(getApplicationContext(), "Response: " + response, Toast.LENGTH_SHORT).show();
        },
                error -> VolleyLog.e(TAG, "Error: " + error.getMessage())) {
        };
        MyApplication.getsInstance().add(deleteReq, tagJsonObj);
    }

    /**
     * \
     * This method is for PatchService.
     */
    private void putRequest() {
        String url = "http://httpbin.org/put";
        StringRequest putReq = new StringRequest(Request.Method.PATCH,
                url,
                response -> {
                    Log.e(TAG, response);
                    Toast.makeText(getApplicationContext(), "Response: " + response, Toast.LENGTH_SHORT).show();
                },
                error -> VolleyLog.e(TAG, "Error: " + error.getMessage())
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Alif");
                params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        MyApplication.getsInstance().add(putReq, tagJsonObj);
    }

    /**
     * \
     * This method is for postService.
     */
    private void postService() {
        //Change with your url.
        String urlSignIn = "https://api.androidhive.info/volley/person_object.json";
        JSONObject detail = new JSONObject();
        try {
            detail.put("email", "softices@gmail.com");
            detail.put("password", "123123");
        } catch (JSONException e) {
            Log.e("in catch", e.toString());
        }
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("instructor", detail);
            Log.e("in try", jsonObj.toString());
        } catch (JSONException e) {
            Log.e("in catch", e.toString());
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                urlSignIn, jsonObj,
                response -> {
                    Log.e(TAG, response.toString());
                    Toast.makeText(getApplicationContext(), "Response: "
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }, error -> VolleyLog.e(TAG, "Error: " + error.getMessage())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mail", "softices@gmail.com");
                params.put("password", "123123");
                return params;
            }

        };
        MyApplication.getsInstance().add(req, tagJsonObj);
    }

    /**
     * \
     * This method is for GetService.
     */
    private void getResponse() {
        String urlJsonObj = "https://api.androidhive.info/volley/person_object.json";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, urlJsonObj, null,
                response -> {
                    Log.e(TAG, response.toString());
                    try {
                        Toast.makeText(getApplicationContext(), "Response: " + response,
                                Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }, error -> {
            VolleyLog.e(TAG, "Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_SHORT).show();
        });
        MyApplication.getsInstance().add(req);
    }

    /**
     * \
     * This method functions when clicked on toolbar items
     *
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
