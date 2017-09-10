package com.example.pinalmeruliya.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.TextView;

import java.util.Calendar;

import android.view.View.OnClickListener;

public class TimeEntryActivity extends Activity {
    TimePicker timepicker;
    Button button;
    TextView textView;
    private TextView time;
    private Calendar calendar;
    private String format = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timepicker);
        textView=(TextView)findViewById(R.id.textView1);
        timepicker=(TimePicker)findViewById(R.id.timePicker1);

        timepicker.setIs24HourView(true);
        button=(Button)findViewById(R.id.button1);

        textView.setText(getCurrentTime());

        button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                textView.setText(getCurrentTime());
                Intent i = new Intent(TimeEntryActivity.this, TimeExitActivity.class);
                startActivity(i);
               // int hour = calendar.get(Calendar.HOUR_OF_DAY);
                //int min = calendar.get(Calendar.MINUTE);
                //showTime(hour, min);

            }
        });



    }

    public String getCurrentTime(){
        String currentTime="Your arrival time is "+timepicker.getCurrentHour()+":"+timepicker.getCurrentMinute();
        return currentTime;
        //String currentTime="your booking time";
        //return currentTime;
    }

    public void setTime(View view) {
        int hour = timepicker.getCurrentHour();
        int min = timepicker.getCurrentMinute();
        showTime(hour, min);
    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        time.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
    }




}




