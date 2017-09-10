package com.example.pinalmeruliya.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;
import com.example.pinalmeruliya.myapplication.helper.BookingData;
import com.example.pinalmeruliya.myapplication.webservices.NetworkUtils;
import com.example.pinalmeruliya.myapplication.webservices.ParkMyCarAPI;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BookingHistoryActivity extends AppCompatActivity {
    private List<BookingData> listitems = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private HistoryAdapter mAdapter;
    ProgressDialog progressDialog;
    public static String displayreport;
    public static String Qrcode;
    public static String parkingid;
    public static String vehicletype;
    public static String bookingid;
    public static String qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);


        fetchparkings();
    }


    public void fetchparkings()

    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        HashMap<String, String> params = new HashMap<>();
        params.put("User_id", AndroidUtils.getLoginData(this).getUserID());


        NetworkUtils.sendVolleyPostRequest(ParkMyCarAPI.displayhistory, new NetworkUtils.VolleyCallbackString() {
            @Override
            public void onSuccess(String result) {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.d("error", result);

                JSONObject mainObject = null;
                try {
                    mainObject = new JSONObject(result);
                    JSONArray bookings = mainObject.getJSONArray("bookings");
                    Log.d("booking", String.valueOf(bookings));
                    for (int i = 0; i < bookings.length(); i++) {
                        JSONObject o = bookings.getJSONObject(i);
                        BookingData bookingData = new BookingData(
                                o.getString("S_name"),
                                o.getString("Booking_date"),
                                o.getString("Time_arrive"),
                                o.getString("Time_exit"),
                                o.getString("Entry_time"),
                                o.getString("Exit_time"),
                                o.getString("Booking_Vehicle_2"),
                                o.getString("Paypal_id"),

                                o.getString("Qrcode_random"),
                                o.getString("Booking_id"),
                                o.getString("Parking_id"),
                                o.getString("Booking_amount")


                        );
                        listitems.add(bookingData);
                    }

                    adapter = new HistoryAdapter(getApplicationContext(), listitems);
                    recyclerView.setAdapter(adapter);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {

            }
        }, this, params);
    }


    public class HistoryAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<BookingData> HistoriesList;
        private Context context;

        public HistoryAdapter(Context context, List<BookingData> historiesList) {
            this.context = context;
            HistoriesList = historiesList;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history, null, false);
            layoutView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            MyViewHolder rcv = new MyViewHolder(layoutView);
            return rcv;

        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final BookingData Bookingdata = HistoriesList.get(position);
            holder.parkingname.setText("parkingname:"+Bookingdata.getS_name());
            holder.schedulearrival.setText("actualentrytime:" + Bookingdata.getEntry_time());
            holder.scheduleexit.setText("actualexittime:" + Bookingdata.getExit_time());
            holder.actualarrive.setText("Scheduledtime:" + Bookingdata.getTime_arrive());
            holder.actualexit.setText("Scheduledexittime:" + Bookingdata.getTime_exit());
            holder.amount.setText("bookingamount:" + Bookingdata.getBooking_amount());
            holder.historydate.setText("date:" + Bookingdata.getBooking_date());

            if (Bookingdata.getBooking_Vehicle_2().equals(String.valueOf(0))) {
                holder.vehicletype.setText("vehicle:" + "F");
            } else {
                holder.vehicletype.setText("vehicle:" + "T");
            }

            holder.paypaid.setText("paypalid:" + Bookingdata.getPaypal_id());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Qrcode =Bookingdata.getQr_random();
                    bookingid=Bookingdata.getBooking_id();
                    parkingid=Bookingdata.getParking_id();

                    if (Bookingdata.getBooking_Vehicle_2().equals(String.valueOf(0))) {
                        vehicletype="F";
                    } else {
                         vehicletype="T";
                    }
                    qr_code = "{"+"parking_id"+":"+parkingid+","+"booking_id"+":"+Qrcode+","+"qrcode_random"+":"+bookingid+","+"vehicle_type"+":"+vehicletype+"}";

                    Log.d("qrcode",qr_code);
                    Log.d("CLICK", " CLICK");
                    Intent i = new Intent(BookingHistoryActivity.this,QrcodeActivity.class);
                    startActivity(i);
                }
            });

        }


        @Override
        public int getItemCount() {

            return HistoriesList.size();
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView parkingname, bookingid,qrcoderandom,parkingid, historydate, schedulearrival, scheduleexit, actualarrive, actualexit, vehicletype, amount, paypaid;
        public LinearLayout historylayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            historylayout = (LinearLayout) itemView.findViewById(R.id.historylayout);
            parkingname = (TextView) itemView.findViewById(R.id.parkingname);
            historydate = (TextView) itemView.findViewById(R.id.bookingdate);
            schedulearrival = (TextView) itemView.findViewById(R.id.scheduledarrivetime);
            scheduleexit = (TextView) itemView.findViewById(R.id.scheduledexittime);
            actualarrive = (TextView) itemView.findViewById(R.id.actualarrivetime);
            actualexit = (TextView) itemView.findViewById(R.id.actualexittime);
            vehicletype = (TextView) itemView.findViewById(R.id.vehicletype);
            amount = (TextView) itemView.findViewById(R.id.Bookedamount);
            paypaid = (TextView) itemView.findViewById(R.id.paypalid);
        }


        @Override
        public void onClick(View v) {


        }
    }


}


