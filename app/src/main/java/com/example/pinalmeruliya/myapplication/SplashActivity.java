package com.example.pinalmeruliya.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;

import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;

public class SplashActivity extends Activity {

    // SplashActivity screen timer
    private static int SPLASH_TIME_OUT = 3000;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {




            public void run() {

            if(AndroidUtils.getLoginData(SplashActivity.this).getUserID().equals(""))

            {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);

            }else
            {
                Intent i = new Intent(SplashActivity.this, TimeSlotActivity.class);
                startActivity(i);
            }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}