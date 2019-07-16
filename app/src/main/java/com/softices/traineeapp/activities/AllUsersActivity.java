package com.softices.traineeapp.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softices.traineeapp.R;
import com.softices.traineeapp.adapter.UserAdapter;
import com.softices.traineeapp.database.DatabaseManager;
import com.softices.traineeapp.models.UserModel;
import com.softices.traineeapp.sharedPreferences.AppPref;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerUsers;
    private ArrayList<UserModel> userModelList = new ArrayList<>();
    private UserAdapter adapter;
    private DatabaseManager dbManager;

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
        dbManager = new DatabaseManager(this);

        setAllUsersList();

        getUsers();
    }

    private void setAllUsersList() {
        recyclerUsers = findViewById(R.id.recycler_users);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(AllUsersActivity.this));
        recyclerUsers.setNestedScrollingEnabled(false);
        recyclerUsers.setHasFixedSize(true);
        adapter = new UserAdapter(userModelList, this);
        recyclerUsers.setAdapter(adapter);
    }

    /**
     * \
     * Set data in list & set adapter &
     * shows all register users.
     */
    private void getUsers() {
        String mail = AppPref.getUserEmail(AllUsersActivity.this);
        userModelList.addAll(dbManager.getAllUserData(mail));
        adapter = new UserAdapter(userModelList, this);
        recyclerUsers.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                finish();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
