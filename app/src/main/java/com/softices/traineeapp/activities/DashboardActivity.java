package com.softices.traineeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softices.traineeapp.R;
import com.softices.traineeapp.database.DatabaseManager;
import com.softices.traineeapp.models.UserModel;
import com.softices.traineeapp.sharedPreferences.AppPref;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Intent intent;
    private TextView tvWelcome, tvProfileName, tvDetail;
    private ImageView ivProfile;
    private DatabaseManager dbManager;
    private UserModel model;
    private String TAG = "DashboardActivity", currentMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbManager = new DatabaseManager(this);
        model = new UserModel();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String currentMail = AppPref.getUserEmail(DashboardActivity.this);
        tvWelcome = (TextView) findViewById(R.id.tv_welcome);
        model = dbManager.getUserDataByEmail(currentMail);
        tvWelcome.setText("Welcome " + model.getFirstName());

        // fetching & displaying as profile name.
        View view = navigationView.getHeaderView(0);
        tvProfileName = (TextView) view.findViewById(R.id.tv_name);

        view = navigationView.getHeaderView(0);
        tvDetail = (TextView) view.findViewById(R.id.tv_detail);
        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        // fetching & displaying as profile image.
        view = navigationView.getHeaderView(0);
        ivProfile = (ImageView) view.findViewById(R.id.imageView);
    }

    private void setDefaultData() {
        currentMail = AppPref.getUserEmail(this);
        model = dbManager.getUserDataByEmail(currentMail);
        ivProfile.setImageBitmap(model.getPhoto());
        tvProfileName.setText(model.getFirstName());
    }

    @Override
    protected void onResume() {
        setDefaultData();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log_out) {
            AppPref.clearAppPref(DashboardActivity.this);
            Toast.makeText(DashboardActivity.this, getString(R.string.txt_logout), Toast.LENGTH_SHORT).show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_users) {
            intent = new Intent(DashboardActivity.this, AllUsersActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {
            AppPref.clearAppPref(DashboardActivity.this);
            Toast.makeText(DashboardActivity.this, getString(R.string.txt_logout), Toast.LENGTH_SHORT).show();
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_contact_list) {
            intent = new Intent(DashboardActivity.this, ContactListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_maps) {
            intent = new Intent(DashboardActivity.this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_web_service) {
            intent = new Intent(DashboardActivity.this, WebServicesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_broadcast) {
            intent = new Intent(DashboardActivity.this, BroadcastReceiverActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_dialog_box) {
            intent = new Intent(DashboardActivity.this, DialogBoxActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}