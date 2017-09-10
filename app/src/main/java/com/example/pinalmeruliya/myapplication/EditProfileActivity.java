package com.example.pinalmeruliya.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;
import com.example.pinalmeruliya.myapplication.webservices.NetworkUtils;
import com.example.pinalmeruliya.myapplication.webservices.ParkMyCarAPI;

import org.json.JSONObject;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileactivity);
        editText1 = (EditText)findViewById(R.id.usernameprofile);
        editText2 = (EditText)findViewById(R.id.emailprofile);
        editText3 = (EditText)findViewById(R.id.phoneprofile);


        fetchdetail();
        Button change=(Button)findViewById(R.id.profilepassword);
        Button done=(Button)findViewById(R.id.Done);


                    done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    senddetail();
                    Snackbar.make(view, "Your Profile has been updated successfully", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });


        findViewById(R.id.profilepassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(EditProfileActivity.this, ChangePasswordActivity.class);
                startActivity(i);


            }
        });
    }



    private void fetchdetail() {

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id",AndroidUtils.getLoginData(this).getUserID());



        NetworkUtils.sendVolleyPostRequest(ParkMyCarAPI.fetchprofile, new NetworkUtils.VolleyCallbackString() {


            @Override
                public void onSuccess(String result) {
                Log.d("error",result);

                    try {
                        JSONObject json = new JSONObject(result);
                        String res = json.getString("success");

                        if (res.equals("1")) {

                            editText1.setText(json.getString("user_name"));
                            editText2.setText(json.getString("user_email"));
                            editText3.setText(json.getString("user_phone"));




                        } else if (res.equals("0")) {

                            AndroidUtils.showAlertDialog(EditProfileActivity.this, json.getString("error"));

                        }
                    } catch (Exception e) {

                       e.printStackTrace();
                        AndroidUtils.showToast(EditProfileActivity.this, "Something went wrong !! Please try again !!");
                    }
                }

                @Override
                public void onError(String error) {

                }
            }, this, param);
        }



    private void senddetail() {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_name", editText1.getText().toString());
        params.put("user_email", editText2.getText().toString());
        params.put("user_phone", editText3.getText().toString());


        NetworkUtils.sendVolleyPostRequest(ParkMyCarAPI.updateprofile, new NetworkUtils.VolleyCallbackString() {


            @Override
            public void onSuccess(String result) {
                Log.d("error",result);
                try {
                    JSONObject json = new JSONObject(result);
                    String res = json.getString("success");
                    AndroidUtils.showToast(EditProfileActivity.this,result);
                    if (res.equals("1")) {




                        AndroidUtils.setemail(EditProfileActivity.this, editText2.getText().toString());
                      AndroidUtils.setpassword(EditProfileActivity.this, editText3.getText().toString());


                    } else if (res.equals("0")) {

                        AndroidUtils.showAlertDialog(EditProfileActivity.this, json.getString("error"));

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onError(String error) {

            }
        }, this, params);
    }



}




