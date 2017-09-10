package com.example.pinalmeruliya.myapplication.helper;

/**
 * Created by pinalmeruliya on 22-03-2017.
 */

public class BookingData {
    private String S_name;
    private String Booking_date;
    private String Time_arrive;
    private String Time_exit;
    private String Entry_time;
    private String Exit_time;
    private String Booking_Vehicle_2;

    public BookingData(String s_name, String booking_date, String time_arrive, String time_exit, String entry_time, String exit_time, String booking_Vehicle_2, String paypal_id, String booking_id, String parking_id, String booking_amount, String qr_random) {
        S_name = s_name;
        Booking_date = booking_date;
        Time_arrive = time_arrive;
        Time_exit = time_exit;
        Entry_time = entry_time;
        Exit_time = exit_time;
        Booking_Vehicle_2 = booking_Vehicle_2;
        Paypal_id = paypal_id;
        Qr_random = qr_random;
        this.booking_id = booking_id;
        this.parking_id = parking_id;
        Booking_amount = booking_amount;

    }

    private String Qr_random;
    private String booking_id;
    private String parking_id;

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getParking_id() {
        return parking_id;
    }

    public void setParking_id(String parking_id) {
        this.parking_id = parking_id;
    }




    public String getQr_random() {
        return Qr_random;
    }

    public void setQr_random(String qr_random) {
        Qr_random = qr_random;
    }




   /* public BookingData(String s_name, String paypal_id) {
        S_name = s_name;
        Paypal_id = paypal_id;
    }*/

       public BookingData(String time_arrive, String s_name, String booking_date, String time_exit, String exit_time, String entry_time, String booking_Vehicle_2, String booking_amount, String paypal_id) {
            Time_arrive = time_arrive;
            S_name = s_name;
            Booking_date = booking_date;
            Time_exit = time_exit;
            Exit_time = exit_time;
            Entry_time = entry_time;
            Booking_Vehicle_2 = booking_Vehicle_2;
            Booking_amount = booking_amount;
            Paypal_id = paypal_id;
        }

    // private String Booking_Vehicle_4;
    private String Booking_amount;
    private String Paypal_id;




    public String getBooking_amount() {
        return Booking_amount;
    }

    public void setBooking_amount(String booking_amount) {
        Booking_amount = booking_amount;
    }

   

    public String getS_name() {
        return S_name;
    }

    public void setS_name(String S_name) {
        S_name = S_name;
    }

    public String getPaypal_id() {
        return Paypal_id;
    }

    public void setPaypal_id(String Paypal_id) {
        Paypal_id = Paypal_id;
    }

    public String getBooking_date() {
        return Booking_date;
    }

    public void setBooking_date(String Booking_date) {
        Booking_date = Booking_date;
    }

    public String getBooking_Vehicle_2() {
        return Booking_Vehicle_2;
    }

    public void setBooking_Vehicle_2(String Booking_Vehicle_2) {
        Booking_Vehicle_2 = Booking_Vehicle_2;
    }




    public String getEntry_time() {
        return Entry_time;
    }

    public void setEntry_time(String Entry_time) {
        Entry_time = Entry_time;
    }

    public String getExit_time() {
        return Exit_time;
    }

    public void setExit_time(String Exit_time) {
        Exit_time = Exit_time;
    }



    public String getTime_arrive() {
        return  Time_arrive;
    }

    public void setTime_arrive(String  Time_arrive) {
        Time_arrive =  Time_arrive;
    }


    public String getTime_exit() {
        return  Time_exit;
    }

    public void setTime_exit(String  Time_exit) {
        Time_exit =  Time_exit;
    }

}
