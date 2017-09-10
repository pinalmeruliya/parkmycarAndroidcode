package com.example.pinalmeruliya.myapplication;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;
import com.example.pinalmeruliya.myapplication.webservices.NetworkUtils;
import com.example.pinalmeruliya.myapplication.webservices.ParkMyCarAPI;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ConfirmationActivity extends AppCompatActivity {

    NotificationCompat.Builder notification;
    private static final int uniqueID = 123;
    ProgressDialog progressDialog;
    public static String slot_date;
    public static String slot_time;
    public static String random;
    public static String Booking_id;
    public static String amount;
    public static String parking_id;
    public static String paypal_id;
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
        setContentView(R.layout.activity_confirmation);
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        TextView textView = (TextView)findViewById(R.id.QRCODEishere);


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

        Intent intent = getIntent();

        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));

        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {

        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus = (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount + " Rs.");

        paypal_id = String.valueOf(jsonDetails.getString("id"));
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
        params.put("Paypal_id", paypal_id);

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

                        AndroidUtils.showToast(ConfirmationActivity.this,"your details have been send successfully");
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

                        AndroidUtils.showAlertDialog(ConfirmationActivity.this, json.getString("error"));

                    }
                } catch (Exception e) {
                    AndroidUtils.showToast(ConfirmationActivity.this, "Something went wrong !! Please try again !!");
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

/*

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(ConfirmationActivity.this, SearchParkingActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(ConfirmationActivity.this, QrcodeActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {

            Intent i = new Intent(ConfirmationActivity.this, SupportActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {

            startActivity(new Intent(ConfirmationActivity.this, EditProfileActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

            AndroidUtils.logout(this);
            startActivity(new Intent(ConfirmationActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

*/


public void notificationshow(View view)
{
    notification.setSmallIcon(R.drawable.parking);
    notification.setTicker("Park My Car");
    notification.setWhen(System.currentTimeMillis());
    notification.setContentTitle("Your Booking is confirmed");
    notification.setContentText("your booking QR code is here");

    Intent intent = new Intent(this,ConfirmationActivity.class);
    PendingIntent pendingintent= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    notification.setContentIntent(pendingintent);

    NotificationManager nm =(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    nm.notify(uniqueID,notification.build());
}
}
