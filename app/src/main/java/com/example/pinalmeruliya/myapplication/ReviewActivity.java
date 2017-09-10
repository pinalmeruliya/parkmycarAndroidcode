package com.example.pinalmeruliya.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pinalmeruliya.myapplication.Models.ReviewData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class ReviewActivity extends AppCompatActivity {

    public static final String SelectedParking = "selectedparkingname";
    ArrayList<ReviewData> reviewList;
    ListOrderAdapter adapter;
    String selectedParking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewactivity);
        fetchparkings();

        reviewList = new ArrayList<>();

        adapter = new ListOrderAdapter();
        Button button = (Button) findViewById(R.id.writebutton);
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        selectedParking = BookingDetailsActivity.parking_name;

        findViewById(R.id.writebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ReviewActivity.this, WriteReviewActivity.class);
                startActivity(i);


            }
        });
    }

    public void fetchparkings()

    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://parkmycars.co.in/adminpanel/index.php/HomeController/fetchreviews";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject mainObject = new JSONObject(response);
                            JSONArray reviews = mainObject.getJSONArray("review");


                            Type type = new TypeToken<ArrayList<ReviewData>>() {
                            }.getType();
                            ArrayList<ReviewData> reviewListTemp = new Gson().fromJson(String.valueOf(reviews.toString()), type);

                            if (reviewListTemp != null) {
                                for (int i = 0; i < reviewListTemp.size(); i++) {
                                    if (reviewListTemp.get(i).getParking_name().equalsIgnoreCase(selectedParking)) {
                                        reviewList.add(reviewListTemp.get(i));
                                    }
                                }
                            }


                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast.makeText(ReviewActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReviewActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);

    }

    public class ListOrderAdapter extends BaseAdapter {

        public TextView userreviewtext, userreview,Parking_name;
        public RatingBar userrating;


        public ListOrderAdapter() {
        }

        @Override
        public int getCount() {
            return reviewList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            View rowView;
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) ReviewActivity.this.getSystemService(ReviewActivity.this.LAYOUT_INFLATER_SERVICE);

            if (view == null) {
                rowView = layoutInflater.inflate(R.layout.list_card, viewGroup, false);
            } else {
                rowView = (View) view;
            }
            findId(rowView);
            fillData(position);

            return rowView;
        }

        private void fillData(final int i) {
            userreviewtext.setText(reviewList.get(i).getReview());
            // userrating.setText("$" + reviewList.get(i).getOrder_amount().toString());
            userreview.setText(reviewList.get(i).getUser_name());


        }

        private void findId(View rowView) {
            userreviewtext = (TextView) rowView.findViewById(R.id.user_reviewtext);
            userreview = (TextView) rowView.findViewById(R.id.user_name);



        }
    }
}


























/* String[] mobileArray = {"kriyansi-Ketiwadi-service is good.","Dhruvi-Food Way-less parking space","Pinal- parking amenities are not good","umesh ganatra - I have booked parking service for my uncle it was good expercise.",
            "chirag mehta - ACC parking plots - good facility","anna kendrik - mirch masala - so much crowd...","rinkal busa- Laldarwaja SMC parking- good time maanagement","kena vora - bvm good slots for 2- wheelers","krupa shah - apid - cool place..","pujan patel - ketiwadi - Good work...keep it up","Pinal_meruliya- kalupur railway station -it has very good facilities..n enough parking spaces"};*/