package com.example.pinalmeruliya.myapplication;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import  com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.example.pinalmeruliya.myapplication.R;
public class QrcodeActivity extends Activity {

    ImageView imageView;

    public String qrcode=BookingHistoryActivity.qr_code;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodeactivity);
        Button button =(Button)findViewById(R.id.qrcode);
        imageView =(ImageView)findViewById(R.id.imageView1);
        Log.d("qrcode",qrcode);


         findViewById(R.id.qrcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try
                {
                    BitMatrix bitMatrix = multiFormatWriter.encode(qrcode,BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imageView.setImageBitmap(bitmap);

                }

                catch (WriterException e) {
                    e.printStackTrace();
                }



            }
        });


    }



}

