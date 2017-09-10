package com.example.pinalmeruliya.myapplication.helper;

/**
 * Created by pinalmeruliya on 20-03-2017.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.icu.util.Calendar;

import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    String selecteddate;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
       // calendar.set(2016, 0, 1);//Year,Mounth -1,Day
        //your_date_picker.setMaxDate(calendar.getTimeInMillis());
        long now = System.currentTimeMillis() - 1000;
      //  dp_time.setMinDate(now);
       // dp_time.setMaxDate(now+(1000*60*60*24*7)); //After 7 Days from Now

        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
       // return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        DatePickerDialog dpDialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
        DatePicker datePicker = dpDialog.getDatePicker();


        datePicker.setMinDate(calendar.getTimeInMillis());//set the current day as the max date
        datePicker.setMaxDate(now+(1000*60*60*24*3));
        return dpDialog;

    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
    }
    public String populateSetDate(int year, int month, int day) {
        selecteddate = day+"/"+month+"/"+year;
        return selecteddate;
        //  dob.setText(month+"/"+day+"/"+year);
    }

}