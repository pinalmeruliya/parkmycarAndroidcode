package com.example.pinalmeruliya.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;
import com.example.pinalmeruliya.myapplication.webservices.NetworkUtils;
import com.example.pinalmeruliya.myapplication.webservices.ParkMyCarAPI;

import org.json.JSONObject;

import java.util.HashMap;

public class TimeExitActivity extends Activity {
    TimePicker timepicker;
    Button button;
    TextView textView;
    TextView date;
    TextView timeentry;
    ProgressDialog progressDialog;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeexit);
        timeentry =(TextView)findViewById(R.id.textView1);
        date = (TextView)findViewById(R.id.textView2);
        textView=(TextView)findViewById(R.id.textViewexit);
        timepicker=(TimePicker)findViewById(R.id.timePickerexit);
        //Uncomment the below line of code for 24 hour view
        timepicker.setIs24HourView(true);
        button=(Button)findViewById(R.id.buttonexit);
        ProgressDialog progressDialog;
        Context context = this;

       // textView.setText(getCurrentTime());

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                textView.setText(getCurrentTime());
                Intent i = new Intent(TimeExitActivity.this, PaymentActivity.class);
                startActivity(i);
            }
        });

    }
    public String getCurrentTime(){
        String currentTime="Your Exit time is "+timepicker.getCurrentHour()+":"+timepicker.getCurrentMinute();
        return currentTime;
        //String currentTime="your booking time";
        //return currentTime;


    }
    private void save() {
        if (true) {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            HashMap<String, String> params = new HashMap<>();
            params.put("Date", date.getText().toString());
            params.put("Time_arrive", timeentry.getText().toString());
            params.put("Time_exit", textView.getText().toString());

            // Intent intent = new Intent(RegisterActivity.this, EditProfileActivity.class);
            //intent.putExtra("name",userFname.getText().toString());


            NetworkUtils.sendVolleyPostRequest(ParkMyCarAPI.bookinginsert, new NetworkUtils.VolleyCallbackString() {
                @Override
                public void onSuccess(String result) {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    try {
                        JSONObject json = new JSONObject(result);
                        String res = json.getString("success");

                        if (res.equals("1")) {

                            Context context = getApplicationContext();
                            CharSequence text = "Your Review has been submitted";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();




                        } else if (res.equals("0")) {

                            AndroidUtils.showAlertDialog(context, json.getString("error"));

                        }
                    } catch (Exception e) {
                        AndroidUtils.showToast(context, "Something went wrong !! Please try again !!");
                    }
                }

                @Override
                public void onError(String error) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        AndroidUtils.showToast(context, error);
                    }
                }
            }, this, params);

        }
    }

}

