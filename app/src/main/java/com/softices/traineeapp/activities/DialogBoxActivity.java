package com.softices.traineeapp.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.softices.traineeapp.R;

public class DialogBoxActivity extends AppCompatActivity {
    private TextView tvTime, tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_box);

        init();
    }

    /** \
     * Initializes all views.
     */
    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        tvTime = findViewById(R.id.tv_time);
        tvDate = findViewById(R.id.tv_date);
    }

    /** \
     * Invokes when show dialogue button is clicked.
     * @param v
     */
    public void alertDialog(View v) {
        final DialogInterface.OnClickListener di;
        di = (dialog, i) -> {
            String a;
            if (i == dialog.BUTTON_POSITIVE) {
                a = tvDate.getText().toString();
                if (a.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_no_date), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_date_selected), Toast.LENGTH_LONG).show();
                }
            } else {
                a = tvTime.getText().toString();
                if (a.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_no_time), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_time_selected), Toast.LENGTH_LONG).show();
                }
            }
        };
        AlertDialog.Builder ab;
        ab = new AlertDialog.Builder(this);
        ab.setTitle("Alert Dialog");
        ab.setPositiveButton("Date", di);
        ab.setNeutralButton("Time", di);
        AlertDialog ad = ab.create();
        ad.show();
    }


    /** \
     * Opens DatePicker on button clicked.
     * @param v
     */
    public void date(View v) {
        DatePickerDialog.OnDateSetListener dpd_l;
        dpd_l = (view, year, month, dayOfMonth) -> {
            int Month = month + 1;
            tvDate.setText(dayOfMonth + "/" + Month + "/" + year);
        };
        DatePickerDialog dpd;
        dpd = new DatePickerDialog(this, dpd_l, 2018, 2, 1);
        dpd.show();
    }

    /** \
     * Opens TimePicker when button clicked.
     * @param v
     */
    public void time(View v) {
        TimePickerDialog.OnTimeSetListener tpd_l;
        tpd_l = (view, h, m) -> {
            String a = "";
            if (h >= 12) {
                a = "PM";
                h = h - 12;
            } else {
                a = "AM";
            }
            tvTime.setText(h + ":" + m + " " + a);
        };
        TimePickerDialog tpd;
        tpd = new TimePickerDialog(this, tpd_l, 17, 54, false);
        tpd.show();
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