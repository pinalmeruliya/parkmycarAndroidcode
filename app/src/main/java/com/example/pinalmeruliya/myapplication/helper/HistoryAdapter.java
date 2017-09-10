package com.example.pinalmeruliya.myapplication.helper;

/**
 * Created by pinalmeruliya on 25-03-2017.
 */


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pinalmeruliya.myapplication.QrcodeActivity;
import com.example.pinalmeruliya.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private List<BookingData> HistoriesList;
    private Context context ;

    public HistoryAdapter(Context context, List<BookingData> historiesList) {
        this.context = context;
        HistoriesList = historiesList;
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public static TextView parkingname, bookingdate, historydate, schedulearrival, scheduleexit, actualarrive, actualexit, vehicletype, amount, paypaid;
        public LinearLayout historylayout;

        public MyViewHolder(View view) {
            super(view);
            historylayout = (LinearLayout) view.findViewById(R.id.historylayout);
            parkingname = (TextView) view.findViewById(R.id.parkingname);
            historydate = (TextView) view.findViewById(R.id.bookingdate);
            schedulearrival = (TextView) view.findViewById(R.id.scheduledarrivetime);
            scheduleexit = (TextView) view.findViewById(R.id.scheduledexittime);
            actualarrive = (TextView) view.findViewById(R.id.actualarrivetime);
            actualexit = (TextView) view.findViewById(R.id.actualexittime);
            vehicletype = (TextView) view.findViewById(R.id.vehicletype);
            amount = (TextView)view.findViewById(R.id.Bookedamount);
            paypaid = (TextView)view.findViewById(R.id.paypalid);
        }
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
       final BookingData Bookingdata = HistoriesList.get(position);
        holder.parkingname.setText("parkingname:"+ Bookingdata.getS_name());
      holder.schedulearrival.setText("actualentrytime:"+Bookingdata.getEntry_time());
        holder.scheduleexit.setText("actualexittime:" +Bookingdata.getExit_time());
        holder.actualarrive.setText("Scheduledtime:"+Bookingdata.getTime_arrive());
        holder.actualexit.setText("Scheduledexittime:"+Bookingdata.getTime_exit());
        holder.amount.setText("bookingamount:"+Bookingdata.getBooking_amount());
        holder.historydate.setText("date:"+Bookingdata.getBooking_date());
        if(Bookingdata.getBooking_Vehicle_2().equals(String.valueOf(0)))
        {
            holder.vehicletype.setText("vehicle:"+"F");
        }
        else
        {
            holder.vehicletype.setText("vehicle:"+"T");
        }

        holder.paypaid.setText("paypalid:"+Bookingdata.getPaypal_id());


    }



    @Override
    public int getItemCount() {

       return HistoriesList.size();
    }
}