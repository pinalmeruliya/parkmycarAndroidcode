package com.example.pinalmeruliya.myapplication.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.example.pinalmeruliya.myapplication.Models.Users;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class AndroidUtils {

    public static String LOGINDETAIL = "LoginDetail";
    public static String USERPASSWORD = "userPassword";
    public static String USEREMAIL = "userEmail";
    public static String USERID ="userid";

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {

            AndroidUtils.showToast(context, "Please connect to internet.");
            return false;
        }
    }

    public static String generateRandom() {
        Random random = new Random();
        return "/" + random.nextInt((999999999 - 100000000) + 1) + 100000000;
    }

    public static void showAlertDialog(final Context context, String title) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("Park My Car");
        alertDialog.setMessage(title);
      //  alertDialog.setCancelable(true);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();

    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }



    public static void deleteLoginData(Context context)
    {
      SharedPreferences sharedpreferences = context.getSharedPreferences(
                LOGINDETAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
       editor.apply();
    }

    public static void setuserid(Context context, String userid) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(
                LOGINDETAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString(USERID, userid);

        editor.apply();
    }


    public static void setpassword(Context context, String password) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(
                LOGINDETAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(USERPASSWORD, password);

        editor.apply();
    }

    public static void logout(Context logout)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(logout);
        preferences.edit().clear().commit();
    }



    public static void setemail(Context context, String email) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(
                LOGINDETAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

// enter user login data as per requirement in "" fron json object
            editor.putString(USEREMAIL, email);


        editor.apply();
    }

    public static Users getLoginData(Context context)
    {
        Users users=null;
        try {
            SharedPreferences sharedpreferences = context.getSharedPreferences(
                    LOGINDETAIL, Context.MODE_PRIVATE);
             users = new Users();
             users.setUserEmail(sharedpreferences.getString(USEREMAIL, ""));
             users.setuserPassword(sharedpreferences.getString(USERPASSWORD, ""));
            users.setUserID(sharedpreferences.getString(USERID,""));
        } catch (Exception e) {
            e.printStackTrace();
        }
return users;
    }
}





