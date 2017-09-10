package com.example.pinalmeruliya.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pinalmeruliya.myapplication.helper.ConstantData;
import com.example.pinalmeruliya.myapplication.helper.ParkData;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class BookingDetailsActivity extends Activity {
    ParkData parkData;
    LatLng currentLatLong;
    public static String selected_2charge;
    public static String selected_4charge;
    public static String parking_ID;
    public static String parking_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        parkData= ConstantData.selecteParkData;
        ConstantData.selecteParkData= null;
        currentLatLong=ConstantData.currentLatLong;
        ConstantData.currentLatLong=null;
        TextView tvTitleAddress= (TextView) findViewById(R.id.textViewTitleAddress);
        TextView subTitleAddress= (TextView) findViewById(R.id.subTitleAddress);
        TextView txtprice= (TextView) findViewById(R.id.txtprice);
        TextView emailindata = (TextView) findViewById(R.id.emailindata);
        Button review=(Button)findViewById(R.id.Reviews);
        TextView distanceFrom= (TextView) findViewById(R.id.distanceFrom);
        ImageView parkImage= (ImageView) findViewById(R.id.parkImage);
        Glide.with(this).load("http://www.championparkingnyc.com/images/BigStock/116212907[1].jpg").into(parkImage);
        tvTitleAddress.setText(parkData.getS_name()+"-"+parkData.getS_managername());
        parking_ID= parkData.getParking_id();
        parking_name = parkData.getS_name();
        subTitleAddress.setText(parkData.getS_address());
        findViewById(R.id.Reviews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(BookingDetailsActivity.this, ReviewActivity.class);
                startActivity(i);


            }
        });
        txtprice.setText("2w :"+parkData.getS_2wheel()+"  ,4w : "+parkData.getS_4wheel()+"Per Hour");
       // Intent i = new Intent(BookingDetailsActivity.this, CalendarActivity.class);
        selected_4charge = parkData.getS_4wheel();
        selected_2charge = parkData.getS_2wheel();
        Log.d("error",selected_2charge+ selected_4charge);

        emailindata.setText("Contact:"+parkData.getS_emailid()+","+parkData.getS_contact());
        if (isLocationNil()) {
            distanceFrom.setText("few Km");
        }
        else
        {
            try{
                distanceFrom.setText(""+ SearchParkingActivity.distance(Double.parseDouble(parkData.getS_lati()),
                        Double.parseDouble(parkData.getS_longi()),
                        currentLatLong.latitude,
                        currentLatLong.longitude));
            }catch (Exception ex)
            {
                distanceFrom.setText("few Km");
            }
        }
        findViewById(R.id.book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   Intent i = new Intent(BookingDetailsActivity.this, CalendarActivity.class);
                startActivity(i);


            }
        });
        findViewById(R.id.navigate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocationNil())
                {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.parseDouble(parkData.getS_lati()),
                            Double.parseDouble(parkData.getS_longi()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
                else
                {
                    String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+currentLatLong.latitude+","+currentLatLong.longitude+"&daddr="+parkData.getS_lati()+","+parkData.getS_longi();

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(Intent.createChooser(intent, "Select an application"));
                }

            }
        });
    }
    private boolean isLocationNil() {
        if (currentLatLong == null)
            return true;
        else {
            return (currentLatLong.latitude == 0.0 || currentLatLong.longitude == 0.0);
        }

    }



}
