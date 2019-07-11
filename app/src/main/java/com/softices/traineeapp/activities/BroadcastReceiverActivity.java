package com.softices.traineeapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.softices.traineeapp.R;

public class BroadcastReceiverActivity extends AppCompatActivity {

    public TextView tvReceiveStatus, tvReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_receiver);

        init();
    }

    /**
     * \
     * initializes all views
     */
    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        tvReceiveStatus = (TextView) findViewById(R.id.tv_receive_status);
        tvReceive = (TextView) findViewById(R.id.tv_receive);
        broadcastAction();
    }

    /**
     * \
     * Sends action for braodcast.
     */
    private void broadcastAction() {
        IntentFilter filter;
        filter = new IntentFilter();

        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(myReceiver, filter);
    }

    /**
     * \
     * registering Broadcast receiver within the class.
     */
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = "";
            String action = intent.getAction();

            if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
                tvReceive.setVisibility(View.GONE);
                msg = "Plug Connected...";
            } else {
                tvReceive.setText("Please Plug-in your mobile !!");
                msg = "Plug Disconnected..";
            }
            tvReceiveStatus.setText(msg);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    };

    /** \
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

    @Override
    protected void onDestroy() {
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }
}