package com.example.pinalmeruliya.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;
import com.example.pinalmeruliya.myapplication.webservices.NetworkUtils;
import com.example.pinalmeruliya.myapplication.webservices.ParkMyCarAPI;

import org.json.JSONObject;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldpass;
    EditText newpass;
    EditText renewpass;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        Button done=(Button)findViewById(R.id.newpassword);

        oldpass=(EditText)findViewById(R.id.oldpass);
        newpass=(EditText)findViewById(R.id.newpass);
        renewpass=(EditText)findViewById(R.id.repass);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                save();

            }
        });

    }

    private void save() {
        if (toVerify()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Updating...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            HashMap<String, String> params = new HashMap<>();
            params.put("user_id",AndroidUtils.getLoginData(this).getUserID());
            params.put("user_password", oldpass.getText().toString());
            params.put("user_newpassword", newpass.getText().toString());
            // Intent intent = new Intent(RegisterActivity.this, EditProfileActivity.class);
            //intent.putExtra("name",userFname.getText().toString());


            NetworkUtils.sendVolleyPostRequest(ParkMyCarAPI.changepassword, new NetworkUtils.VolleyCallbackString() {
                @Override
                public void onSuccess(String result) {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Log.d("error",result);
                    try {
                        JSONObject json = new JSONObject(result);
                        String res = json.getString("success");

                        if (res.equals("1")) {

                            Context context = getApplicationContext();
                            CharSequence text = "Your password has been updated successfully";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();


                            AndroidUtils.setemail(ChangePasswordActivity.this, newpass.getText().toString());



                        } else if (res.equals("0")) {

                            AndroidUtils.showAlertDialog(ChangePasswordActivity.this, json.getString("error"));

                        }
                    } catch (Exception e) {
                        AndroidUtils.showToast(ChangePasswordActivity.this, "Something went wrong !! Please try again !!");
                    }
                }

                @Override
                public void onError(String error) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        AndroidUtils.showToast(ChangePasswordActivity.this, error);
                    }
                }
            }, this, params);

        }
    }

    private boolean toVerify() {

        boolean flag = true;
        if (oldpass.getText().toString().isEmpty()) {
            oldpass.setError("Old password is required.");
            oldpass.requestFocus();
            flag = false;
        }
        else if (oldpass.getText().toString().length() < 4) {
            oldpass.setError("Password must be atleat 4 characters long.");
            oldpass.requestFocus();
            flag = false;

        } else if (newpass.getText().toString().length() < 4) {
            newpass.setError("Password must be atleat 4 characters long.");
            newpass.requestFocus();
            flag = false;

        }
        else if (renewpass.getText().toString().length() < 4) {
            renewpass.setError("Password must be atleat 4 characters long.");
            renewpass.requestFocus();
            flag = false;

        }

        else if (!renewpass.getText().toString().equals(renewpass.getText().toString())) {
            renewpass.setError("Passwords do not match.");
            renewpass.requestFocus();
            flag = false;

        }

        return flag;
    }

}
