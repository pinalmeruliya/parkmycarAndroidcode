package com.example.pinalmeruliya.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;


public class TimeSlotActivity extends AppCompatActivity {

    public static EditText timeslotdate;
    public static EditText timeslottime;
    public static String slot_date;
    public static String slot_time;
    static Context context;

    public static Button btncontinue;

   // Context context = this;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);
        context= this;
        timeslotdate = (EditText) findViewById(R.id.editText_date_slot);
        timeslottime = (EditText) findViewById(R.id.editText_time_slot);
        btncontinue = (Button) findViewById(R.id.Continue);
        calendar = Calendar.getInstance();


        timeslotdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getSupportFragmentManager(), "DatePicker");

            }
        });




        timeslottime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timepicker");


            }

        });


        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(toVerify()) {
                        slot_date = timeslotdate.getText().toString();
                        slot_time = timeslottime.getText().toString();

                        Intent i = new Intent(TimeSlotActivity.this, SearchParkingActivity.class);
                        startActivity(i);
                    }


            }
        });

    }

    private boolean toVerify() {

        boolean flag = true;
        if (timeslotdate.getText().toString().isEmpty()) {
            timeslotdate.setError("Date is required.");
            timeslotdate.requestFocus();
            flag = false;
        } else if (timeslottime.getText().toString().isEmpty()) {
            timeslottime.setError("Time is required.");
            timeslottime.requestFocus();
            flag = false;
        }


        return flag;
    }





    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        String selecteddate;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();

            long now = System.currentTimeMillis() - 1000;


            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpDialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            DatePicker datePicker = dpDialog.getDatePicker();


            datePicker.setMinDate(calendar.getTimeInMillis());//set the current day as the max date
            datePicker.setMaxDate(now + (1000 * 60 * 60 * 24 * 3));
            return dpDialog;

        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {

            timeslotdate.setText(year + "/" + month + "/" + day);

        }

    }




    public static class TimePickerFragment extends android.support.v4.app.DialogFragment
            implements TimePickerDialog.OnTimeSetListener {




        public TimePickerFragment() {
            // Required empty public constructor
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker

            final java.util.Calendar calendar = java.util.Calendar.getInstance();
            int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
            int minute = calendar.get(java.util.Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String am_pm = "";

            Calendar datetime = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime.set(Calendar.MINUTE, minute);

            if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            {
                am_pm = "AM";
                if((datetime.get(Calendar.HOUR) >= 0  ) && datetime.get(Calendar.HOUR) <=6 )
                {

                    AndroidUtils.showAlertDialog(context, "Your service is not available between 12 AM to 7 AM");


                }
                btncontinue.setVisibility(View.VISIBLE);

            }
            else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";
            btncontinue.setVisibility(View.VISIBLE);
            String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
            String strMinToShow = (datetime.get(Calendar.MINUTE) < 10) ? "0" + datetime.get(Calendar.MINUTE) : datetime.get(Calendar.MINUTE) + "";


            timeslottime.setText(strHrsToShow + ":" + strMinToShow + " " + am_pm);


        }

    }
}


