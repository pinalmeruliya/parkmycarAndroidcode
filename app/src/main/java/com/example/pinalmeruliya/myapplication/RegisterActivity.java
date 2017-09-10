package com.example.pinalmeruliya.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;
import com.example.pinalmeruliya.myapplication.webservices.NetworkUtils;
import com.example.pinalmeruliya.myapplication.webservices.ParkMyCarAPI;

import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userFname;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userphone;
    private EditText userConfirmpassword;
    private Button btnSubmit;
    ProgressDialog progressDialog;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        userFname = (EditText) findViewById(R.id.user_fname);
        userEmail = (EditText) findViewById(R.id.user_email);
        userPassword = (EditText) findViewById(R.id.user_password);
        userConfirmpassword = (EditText) findViewById(R.id.user_confirmpassword);
        userphone = (EditText) findViewById(R.id.user_phone);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
     btnSubmit.setOnClickListener(this);
    }


    public void onClick(View v) {
            if (v == btnSubmit) {
                save();
            }
        }






    private void save() {
        if (toVerify()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Saving...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            HashMap<String, String> params = new HashMap<>();
            params.put("user_name", userFname.getText().toString());
            params.put("user_email", userEmail.getText().toString());
            params.put("user_phone", userphone.getText().toString());
            params.put("user_password", userPassword.getText().toString());


            NetworkUtils.sendVolleyPostRequest(ParkMyCarAPI.register, new NetworkUtils.VolleyCallbackString() {
                @Override
                public void onSuccess(String result) {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    try {
                        JSONObject json = new JSONObject(result);
                        String res = json.getString("success");

                        if (res.equals("1")) {

                            Intent i = new Intent(context, TimeSlotActivity.class);
                            startActivity(i);


                            finish();


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

    private boolean toVerify() {

        boolean flag = true;
        if (userFname.getText().toString().isEmpty()) {
            userFname.setError("Name is required.");
            userFname.requestFocus();
            flag = false;
        } else if (!AndroidUtils.isValidEmail(userEmail.getText())) {
            userEmail.setError("Enter valid email.");
            userEmail.requestFocus();
            flag = false;
        } else if (userPassword.getText().toString().length() < 4) {
            userPassword.setError("Password must be atleast 4 characters long.");
            userPassword.requestFocus();
            flag = false;

        } else if (!userConfirmpassword.getText().toString().equals(userPassword.getText().toString())) {
            userConfirmpassword.setError("Passwords do not match.");
            userConfirmpassword.requestFocus();
            flag = false;

        }

        return flag;
    }



}
