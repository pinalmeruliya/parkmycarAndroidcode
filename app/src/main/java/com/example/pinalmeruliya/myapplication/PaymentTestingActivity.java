package com.example.pinalmeruliya.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;
import com.example.pinalmeruliya.myapplication.webservices.NetworkUtils;
import com.example.pinalmeruliya.myapplication.webservices.ParkMyCarAPI;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentTestingActivity extends AppCompatActivity {

    public static String slot_date;
    public static String slot_time;
    public static String random;
    public static String Booking_id;
    public static String amount;
    public static String parking_id;
    public static String selected_4charge_selection;
    public static String selected_2charge_selection;
    public static String Booking_Vehicle_2;
    public static String Booking_Vehicle_4;
    public static String qr_json_string="";
    public  ImageView QRcode;
    TextView textViewId;
    TextView textViewStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testingpayment);
        QRcode =(ImageView)findViewById(R.id.QRCode);

        slot_date = TimeSlotActivity.slot_date;
        slot_time = TimeSlotActivity.slot_time;
        amount = CalendarActivity.amount;
        parking_id = BookingDetailsActivity.parking_ID;
        selected_2charge_selection = BookingDetailsActivity.selected_2charge;
        selected_4charge_selection = BookingDetailsActivity.selected_4charge;


        if(amount.equals(selected_2charge_selection))
        {
            Booking_Vehicle_2 =String.valueOf(1);
            Booking_Vehicle_4 =String.valueOf(0);
        }else
        {
            Booking_Vehicle_4 =String.valueOf(1);
            Booking_Vehicle_2 =String.valueOf(0);
        }

        save();





    }


    private void save() {


            HashMap<String, String> params = new HashMap<>();
            params.put("Booking_date", slot_date);
            params.put("Time_arrive", slot_time);
            params.put("User_id",AndroidUtils.getLoginData(this).getUserID());
            params.put("Parking_id", parking_id);
            params.put("Booking_Vehicle_2",Booking_Vehicle_2);
            params.put("Booking_Vehicle_4", Booking_Vehicle_4);
            params.put("Booking_amount", amount);
            params.put("Paypal_id", "qwertydusihhh");

            // Intent intent = new Intent(RegisterActivity.this, EditProfileActivity.class);
            //intent.putExtra("name",userFname.getText().toString());


            NetworkUtils.sendVolleyPostRequest(ParkMyCarAPI.detailinsert, new NetworkUtils.VolleyCallbackString() {
                @Override
                public void onSuccess(String result) {
                    Log.d("RESULT",result);
                    try {
                        JSONObject json = new JSONObject(result);
                        String res = json.getString("success");



                        if (res.equals("1")) {

                            AndroidUtils.showToast(PaymentTestingActivity.this,"your details have been send successfully");
                            random = json.getString("Qrcode_random");
                            Booking_id =json.getString("Booking_id");


                            JSONObject qrCode=new JSONObject();
                            qrCode.put("parking_id",parking_id);
                            qrCode.put("booking_id",Booking_id);
                            qrCode.put("qrcode_random",random);

                            if(Booking_Vehicle_2.equals("1"))
                            {
                                qrCode.put("vehicle_type","T");
                            }
                            if(Booking_Vehicle_4.equals("1"))
                            {
                                qrCode.put("vehicle_type","F");
                            }



                            qr_json_string=qrCode.toString();


                            Log.d("QRCODE",qr_json_string);

                            Generate_qrCode();


                        } else if (res.equals("0")) {

                            AndroidUtils.showAlertDialog(PaymentTestingActivity.this, json.getString("error"));

                        }
                    } catch (Exception e) {
                        AndroidUtils.showToast(PaymentTestingActivity.this, "Something went wrong !! Please try again !!");
                    }
                }

                @Override
                public void onError(String error) {

                }
            }, this, params);

        }

    public void Generate_qrCode(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try
        {
            BitMatrix bitMatrix = multiFormatWriter.encode(qr_json_string, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            QRcode.setImageBitmap(bitmap);

        }

        catch (WriterException e) {
            e.printStackTrace();
        }


    }
    }



