package com.example.pinalmeruliya.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.bumptech.glide.Glide;
import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;
import com.example.pinalmeruliya.myapplication.webservices.NetworkUtils;
import com.example.pinalmeruliya.myapplication.webservices.ParkMyCarAPI;

import org.json.JSONObject;

import java.util.HashMap;


import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userUsername;
    private EditText userPassword;
    private Button btnLogin;
    ProgressDialog progressDialog;
    Context context = this;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginnew);
        ImageView parkImage= (ImageView) findViewById(R.id.parkImage1);
        Glide.with(this).load("http://parkmycars.co.in/adminpanel/logo.png").into(parkImage);

        findViews();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);


            }
        });

        findViewById(R.id.forgetpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);


            }
        });



    }

    private void findViews() {
        userUsername = (EditText) findViewById(R.id.user_username);
        userPassword = (EditText) findViewById(R.id.user_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister=(Button) findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            login();
        }

    }


    private void login() {
        if (toVerify()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loging in...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            HashMap<String, String> params = new HashMap<>();
            params.put("user_email", userUsername.getText().toString());
            params.put("user_password", userPassword.getText().toString());



            NetworkUtils.sendVolleyPostRequest(ParkMyCarAPI.login, new NetworkUtils.VolleyCallbackString() {
                @Override
                public void onSuccess(String result) {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    try {
                        JSONObject json = new JSONObject(result);
                        String res = json.getString("success");

                        if (res.equals("1")) {

                            AndroidUtils.setuserid(LoginActivity.this, json.getString("user_id"));
                            AndroidUtils.setemail(LoginActivity.this, json.getString("user_email"));
                            AndroidUtils.setpassword(LoginActivity.this, json.getString("user_password"));

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
        if (userUsername.getText().toString().isEmpty()) {
            userUsername.setError("Username/Email is required.");
            userUsername.requestFocus();
            flag = false;
        } else if (userPassword.getText().toString().isEmpty()) {
            userPassword.setError("Password is required.");
            userPassword.requestFocus();
            flag = false;
        }


        return flag;
    }


}
