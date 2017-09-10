package com.example.pinalmeruliya.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;
import com.example.pinalmeruliya.myapplication.webservices.NetworkUtils;
import com.example.pinalmeruliya.myapplication.webservices.ParkMyCarAPI;


import org.json.JSONObject;

import java.util.HashMap;
public class WriteReviewActivity extends AppCompatActivity {
    private EditText userFname;
    private EditText parkingname;
    private EditText review;
    private Button btnSubmit;
    ProgressDialog progressDialog;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writereview);

        Button button = (Button) findViewById(R.id.submitR);


        userFname = (EditText) findViewById(R.id.editText_Uname);
        parkingname = (EditText) findViewById(R.id.editText_place);
        review = (EditText) findViewById(R.id.editText_review);
        findViewById(R.id.submitR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save();

            }
        });
    }

        private void save() {
            if (toVerify()) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Saving...");
                progressDialog.setCancelable(false);
                progressDialog.show();


                HashMap<String, String> params = new HashMap<>();
                params.put("user_name", userFname.getText().toString());
                params.put("parking_name", parkingname.getText().toString());
                params.put("review", review.getText().toString());


                NetworkUtils.sendVolleyPostRequest(ParkMyCarAPI.baseURLfeedback, new NetworkUtils.VolleyCallbackString() {
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

    private boolean toVerify() {

        boolean flag = true;
        if (userFname.getText().toString().isEmpty()) {
            userFname.setError("Name is required.");
            userFname.requestFocus();
            flag = false;
        } else if (parkingname.getText().toString().isEmpty()) {
            parkingname.setError("Parking Name is required.");
            parkingname.requestFocus();
            flag = false;
        } else if (review.getText().toString().isEmpty()) {
            review.setError("Please write Review");
            review.requestFocus();
            flag = false;

        }

        return flag;
    }

}

