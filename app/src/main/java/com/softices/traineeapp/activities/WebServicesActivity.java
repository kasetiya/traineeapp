package com.softices.traineeapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.softices.traineeapp.R;
import com.softices.traineeapp.application.MyApplication;
import com.softices.traineeapp.constants.JsonKeys;
import com.softices.traineeapp.sharedPreferences.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebServicesActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGet, btnPost, btnPatch, btnDelete, btnPostService, btnPatchService,
            btnDeleteService;
    private TextView tvResponse, tvDelete;
    private EditText edtMail, edtPass, edtComment;
    private LinearLayout postLayout, patchLayout, deleteLayout;
    private SessionManager session;
    private String TAG = "WebServicesActivity", jsonResponse, tagJsonObj = "json_obj_req";
    ;

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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        session = new SessionManager(this);
        btnGet = findViewById(R.id.btn_get);
        btnGet.setOnClickListener(this);
        btnPost = findViewById(R.id.btn_post);
        btnPost.setOnClickListener(this);
        btnPatch = findViewById(R.id.btn_patch);
        btnPatch.setOnClickListener(this);
        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
        tvResponse = findViewById(R.id.tv_response);

        postLayout = findViewById(R.id.post_layout);
        edtMail = findViewById(R.id.edt_post_mail);
        edtPass = findViewById(R.id.edt_post_pass);
        btnPostService = findViewById(R.id.btn_post_service);
        btnPostService.setOnClickListener(this);

        patchLayout = findViewById(R.id.patch_layout);
        edtComment = findViewById(R.id.edt_patch);
        btnPatchService = findViewById(R.id.btn_patch_service);
        btnPatchService.setOnClickListener(this);

        deleteLayout = findViewById(R.id.delete_layout);
        tvDelete = findViewById(R.id.tv_delete);
        btnDeleteService = findViewById(R.id.btn_delete_request);
        btnDeleteService.setOnClickListener(this);
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
                tvResponse.setVisibility(View.VISIBLE);
                postLayout.setVisibility(View.GONE);
                patchLayout.setVisibility(View.GONE);
                deleteLayout.setVisibility(View.GONE);
                tvDelete.setVisibility(View.GONE);
                GetResponse();
                break;
            case R.id.btn_post:
                tvResponse.setVisibility(View.GONE);
                patchLayout.setVisibility(View.GONE);
                deleteLayout.setVisibility(View.GONE);
                tvDelete.setVisibility(View.GONE);
                postLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_patch:
                tvResponse.setVisibility(View.GONE);
                postLayout.setVisibility(View.GONE);
                deleteLayout.setVisibility(View.GONE);
                tvDelete.setVisibility(View.GONE);
                tvResponse.setVisibility(View.GONE);
                postLayout.setVisibility(View.GONE);
                patchLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_delete:
                tvResponse.setVisibility(View.GONE);
                postLayout.setVisibility(View.GONE);
                patchLayout.setVisibility(View.GONE);
                deleteLayout.setVisibility(View.VISIBLE);
                tvDelete.setVisibility(View.VISIBLE);
                tvDelete.setText(session.getId());
                break;
            case R.id.btn_post_service:
                postLayout.setVisibility(View.GONE);
                tvResponse.setVisibility(View.VISIBLE);
                tvResponse.setText("");
                PostService();
                break;
            case R.id.btn_patch_service:
                patchLayout.setVisibility(View.GONE);
                PatchRequest();
            case R.id.btn_delete_request:
                tvResponse.setVisibility(View.GONE);
                postLayout.setVisibility(View.GONE);
                patchLayout.setVisibility(View.GONE);
                deleteLayout.setVisibility(View.GONE);
                tvDelete.setVisibility(View.GONE);
                DeleteRequest();
        }
    }

    /**
     * \
     * This method is for DeleteService.
     */
    private void DeleteRequest() {
        String id = session.getId();
        String url = "http://stg.instructorhub.com/api/v1/instructor/comments/" + id + ".json";

        JsonObjectRequest deleteReq = new JsonObjectRequest(Request.Method.DELETE,
                url, null,
                response -> {
                    Log.e(TAG, response.toString());
                    try {
                        String data = response.getString(JsonKeys.jsData);
                        Log.e("onResponse: in try", data);
                    } catch (JSONException e) {
                        Log.e("in catch", e.toString());
                    }
                    Toast.makeText(getApplicationContext(), "Response: " + response.toString(), Toast.LENGTH_SHORT).show();
                },
                error -> VolleyLog.e(TAG, "Error: " + error.getMessage())) {
            @Override
            public Map<String, String> getHeaders() {
                String token = session.getToken();
                Map<String, String> params = new HashMap<String, String>();
                params.put(JsonKeys.jsAccessToken, token);
                return params;
            }
        };
        MyApplication.getsInstance().add(deleteReq, tagJsonObj);

    }

    /**
     * \
     * This method is for PatchService.
     */
    private void PatchRequest() {
        final String text = edtComment.getText().toString().trim();
        String id = session.getId();
        String url = "http://stg.instructorhub.com/api/v1/instructor/comments/" + id + "/edit.json";

        JSONObject jsonText = new JSONObject();
        try {
            jsonText.put("text", text);
        } catch (JSONException e) {
            Log.e("in catch", e.toString());
        }
        JSONObject jsonComment = new JSONObject();
        try {
            jsonComment.put(JsonKeys.jsComment, jsonText);
            Log.e("in try", jsonComment.toString());
        } catch (JSONException e) {
            Log.e("in catch", e.toString());
        }

        JsonObjectRequest patchReq = new JsonObjectRequest(Request.Method.PATCH,
                url, jsonComment,
                response -> {
                    Log.e(TAG, response.toString());
                    try {
                        String data = response.getString(JsonKeys.jsData);
                        Log.e("onResponse: in try", data);
                    } catch (JSONException e) {
                        Log.e("in catch", e.toString());
                    }
                    Toast.makeText(getApplicationContext(), "Response: " + response.toString(), Toast.LENGTH_SHORT).show();
                },
                error -> VolleyLog.e(TAG, "Error: " + error.getMessage())
        ) {
            @Override
            public Map<String, String> getHeaders() {
                String token = session.getToken();
                Map<String, String> params = new HashMap<String, String>();
                params.put(JsonKeys.jsAccessToken, token);
                return params;
            }
        };
        MyApplication.getsInstance().add(patchReq, tagJsonObj);

    }

    /**
     * \
     * This method is for PostService.
     */
    private void PostService() {
        final String mail = edtMail.getText().toString().trim();
        final String pass = edtPass.getText().toString().trim();

        //Change with your url.
        String urlSignIn = "http://stg.instructorhub.com/api/v1/instructor/signin.json";

        JSONObject detail = new JSONObject();
        try {
            detail.put("email", mail);
            detail.put("password", pass);
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
                    try {
                        JSONObject data = response.getJSONObject(JsonKeys.jsData);
                        String token = data.getString(JsonKeys.jsToken);
                        String id = data.getString(JsonKeys.jsId);
                        session.createIdSession(id, true);
                        session.createTokenSession(token, true);
                        tvResponse.setText("TOKEN: " + token);
                        Log.e("in try", token);

                    } catch (JSONException e) {
                        Log.e("in catch", e.toString());
                    }
                    Toast.makeText(getApplicationContext(), "Response: "
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }, error -> VolleyLog.e(TAG, "Error: " + error.getMessage())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(JsonKeys.jsMail, mail);
                params.put(JsonKeys.jsPassword, pass);
                return params;
            }

        };
        MyApplication.getsInstance().add(req, tagJsonObj);
    }

    /**
     * \
     * This method is for GetService.
     */
    private void GetResponse() {
        String urlJsonObj = "https://api.androidhive.info/volley/person_object.json";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, urlJsonObj, null,
                response -> {
                    Log.e(TAG, response.toString());
                    try {
                        String name = response.getString(JsonKeys.jsName);
                        String mail = response.getString(JsonKeys.jsMail);
                        JSONObject phone = response.getJSONObject(JsonKeys.jsPhone);
                        String home = phone.getString(JsonKeys.jsHome);
                        String mobile = phone.getString(JsonKeys.jsMobile);

                        jsonResponse = "";
                        jsonResponse += "Name: " + name + "\n";
                        jsonResponse += "E-mail: " + mail + "\n";
                        jsonResponse += "Phone: \n";
                        jsonResponse += "   Home: " + home + "\n";
                        jsonResponse += "   Mobile: " + mobile + "\n";
                        tvResponse.setText(jsonResponse);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
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
