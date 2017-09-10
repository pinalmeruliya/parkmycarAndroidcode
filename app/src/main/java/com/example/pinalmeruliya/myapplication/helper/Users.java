package com.example.pinalmeruliya.myapplication.helper;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by pinalmeruliya on 01-03-2017.
 */

public class Users {
    public static String LOGINDETAIL = "LoginDetail";
    public static String USERNAME = "userName";
    public static String USEREMAIL = "userEmail";
    public static Users getLoginData(Context context) {
        try {
            SharedPreferences sharedpreferences = context.getSharedPreferences(
                    LOGINDETAIL, Context.MODE_PRIVATE);
            Users users = new Users();
           // users.setUserEmail(sharedpreferences.getString(USEREMAIL, ""));
            //users.setUserName(sharedpreferences.getString(USERNAME, ""));

            return users;
        } catch (Exception e) {
            return null;
        }
    }
}