package com.example.pinalmeruliya.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;
import com.example.pinalmeruliya.myapplication.webservices.NetworkUtils;
import com.example.pinalmeruliya.myapplication.webservices.ParkMyCarAPI;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText Femail;
    private Context context;


    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        Femail =(EditText)findViewById(R.id.editText_Femail);
        findViewById(R.id.submitF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save();

            }
        });
    }

    private void save() {
        if (toVerify()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Sending...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            HashMap<String, String> params = new HashMap<>();
            params.put("user_email", Femail.getText().toString());



            NetworkUtils.sendVolleyPostRequest(ParkMyCarAPI.baseURLforgotpassword, new NetworkUtils.VolleyCallbackString() {
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
                            CharSequence text = "Your password has been sent to this email";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                        }else if (res.equals("0"))
                        {
                            Context context = getApplicationContext();
                            CharSequence text = "This email is not registered with us";
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
        if (Femail.getText().toString().isEmpty()) {
            Femail.setError("Name is required.");
            Femail.requestFocus();
            flag = false;
        }
        return flag;
    }


}
