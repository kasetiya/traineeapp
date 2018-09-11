package com.softices.traineeapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.softices.traineeapp.R;
import com.softices.traineeapp.adapter.UserAdapter;
import com.softices.traineeapp.database.DatabaseHelper;
import com.softices.traineeapp.database.DatabaseManager;
import com.softices.traineeapp.models.UserModel;
import com.softices.traineeapp.sharedPreferences.AppPref;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerUsers;
    private ArrayList<UserModel> userModelList;
    private UserAdapter adapter;
    private DatabaseManager dbManager;
    private SQLiteDatabase dbObject;
    private DatabaseHelper dbHelper;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

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
        dbHelper = new DatabaseHelper(this);
        dbObject = dbHelper.getWritableDatabase();

        userModelList = new ArrayList<>();

        recyclerUsers = (RecyclerView) findViewById(R.id.recycler_users);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(AllUsersActivity.this,
                LinearLayoutManager.VERTICAL, false));
        recyclerUsers.setNestedScrollingEnabled(false);
        recyclerUsers.setHasFixedSize(true);


        adapter = new UserAdapter(userModelList, this);
        recyclerUsers.setAdapter(adapter);
        recyclerUsers.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL ));
        showUsers();

    }

    /**
     * \
     * Set data in list & set adapter &
     * shows all register users.
     */
    private void showUsers() {
        String mail = AppPref.getUserEmail(AllUsersActivity.this);
        userModelList.addAll(dbManager.getAllUserData(mail));
        adapter = new UserAdapter(userModelList, this);
        recyclerUsers.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * \
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