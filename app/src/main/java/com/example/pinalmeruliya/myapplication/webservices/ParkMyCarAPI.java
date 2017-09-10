package com.example.pinalmeruliya.myapplication.webservices;



public class ParkMyCarAPI {
    public static String baseURL = "http://parkmycars.co.in/parkmycar/webservices/";


    public static String baseURLreview =  baseURL +"HomeController/fetchreviews";
    public static String baseURLfeedback= "http://parkmycars.co.in/adminpanel/index.php/HomeController/writereview";
    public static String baseURLforgotpassword=baseURL +"users/forgetpassword";
    public static String bookinginsert = "http://parkmycars.co.in/adminpanel/index.php/HomeController/bookinginsert";
    public static  String fetchprofile = baseURL +"users/getprofileinfo";
    public static  String updateprofile = baseURL +"users/updateprofile";
    public static String login = "http://parkmycars.co.in/adminpanel/index.php/Users/login";
    public static String register = baseURL+"users/register";
    public static String detailinsert = baseURL+"fetch/bookinginsert";
    public static String changepassword = baseURL + "users/changepassword";
    public static String displayhistory =baseURL + "fetch/fetchreport";


}
