package com.example.pinalmeruliya.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.example.pinalmeruliya.myapplication.helper.AndroidUtils;
import com.example.pinalmeruliya.myapplication.helper.ConstantData;
import com.example.pinalmeruliya.myapplication.helper.GPSTracker;
import com.example.pinalmeruliya.myapplication.helper.Jclass;
import com.example.pinalmeruliya.myapplication.helper.ParkData;
import com.example.pinalmeruliya.myapplication.webservices.NetworkUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SearchParkingActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final String PLACE_API_Key = "AIzaSyB6738bzUSJRMlFKCjX5zFpG_fwJIH__Gc";
    LatLng currentLatLong;
    LatLng movementlocation;
    ArrayList<ParkData> parkingList;
    Marker currentLocation;
    private GoogleMap mMap;
    private AutoCompleteTextView et_address;
    private AQuery aq;
    private JSONObject locationObj = null;
    private GPSTracker gpstracker;
    private Marker previousSelectedMarker;
    private ParkData parkData;
    int s5, s3;
    Button book;
    public static int two_wheeeler_count;
    public static int four_wheeler_count;
    public static String selected_date;
    public static String selected_time;

    TextView drawername;
    TextView draweremail;
    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String url = getAutoCompleteUrl(s.toString());
            if (et_address.getText().length() > 3) {
                getPlace(url, et_address);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };
    private ProgressDialog waitingprogress;
    private LinearLayout nolocationlayout;
    private RelativeLayout parkinglayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        book = (Button) findViewById(R.id.book);
        try {


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


            // map

            et_address = (AutoCompleteTextView) findViewById(R.id.et_address);
            nolocationlayout = (LinearLayout) findViewById(R.id.nolocationlayout);
            nolocationlayout.setVisibility(View.GONE);

            parkinglayout = (RelativeLayout) findViewById(R.id.parkinglayout);
            parkinglayout.setVisibility(View.GONE);


            aq = new AQuery(this);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            movementlocation = new LatLng(22.552649789980535, 72.92361259438621);

            et_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationObj.getDouble("lat"), locationObj.getDouble("lng")), 16));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, 1000);
                    et_address.removeTextChangedListener(textWatcher);
                    et_address.clearFocus();
                    hideKeybord();


                }
            });
            et_address.addTextChangedListener(textWatcher);

            et_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (hasFocus)
                        et_address.addTextChangedListener(textWatcher);


                }
            });

            findViewById(R.id.changelocation).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nolocationlayout.setVisibility(View.GONE);
                    et_address.requestFocus();
                }
            });


            findViewById(R.id.navigate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLocationNil()) {
                        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.parseDouble(parkData.getS_lati()),
                                Double.parseDouble(parkData.getS_longi()));
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    } else {
                        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + currentLatLong.latitude + "," + currentLatLong.longitude + "&daddr=" + parkData.getS_lati() + "," + parkData.getS_longi();

                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(Intent.createChooser(intent, "Select an application"));
                    }

                }
            });

            findViewById(R.id.book).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (parkData != null) {
                        ConstantData.selecteParkData = parkData;
                        ConstantData.currentLatLong = currentLatLong;
                        Intent intent = new Intent(SearchParkingActivity.this, BookingDetailsActivity.class);
                        startActivity(intent);
                    }
                }
            });

        } catch (Exception ex) {
            Log.e("park", ex.getMessage());
        }
    }

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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(SearchParkingActivity.this, BookingHistoryActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {

            Intent i = new Intent(SearchParkingActivity.this, SupportActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {

            startActivity(new Intent(SearchParkingActivity.this, EditProfileActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

            AndroidUtils.logout(this);
            AndroidUtils.deleteLoginData(this);

            startActivity(new Intent(SearchParkingActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Setting",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(false);
        alert.show();
    }

    private boolean isLocationNil() {
        if (currentLatLong == null)
            return true;
        else {
            return (currentLatLong.latitude == 0.0 || currentLatLong.longitude == 0.0);
        }
    }

    private void waitingforgps() {

        if (isLocationNil()) {
            if (waitingprogress == null) {
                waitingprogress = ProgressDialog.show(this, "Waiting for GPS...",
                        "", true);
                waitingprogress.setCancelable(false);
            }

            if (!waitingprogress.isShowing()) {
                try {
                    waitingprogress.show();
                } catch (Exception ex) {


                    Toast.makeText(this, "GPS Dialog" + ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (gpstracker != null)

        {
            LocationManager locationManager = null;

            try {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    currentLatLong = new LatLng(gpstracker.latitude, gpstracker.longitude);

                    if (currentLatLong != null && currentLatLong.latitude != 0 && currentLatLong.longitude != 0) {
                        Toast.makeText(this, "update location", Toast.LENGTH_SHORT).show();

                        if (mMap != null) {

                            currentLocation.remove();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 16));
                            currentLocation = mMap.addMarker(new MarkerOptions().position(currentLatLong).title("you are here!!"));
                        }
                        try {
                            waitingprogress.dismiss();
                        } catch (Exception ex) {
//                        Toast.makeText(getActivity(), "GPS Dialog" + ex.toString(), Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        Handler handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                waitingforgps();
                            }
                        }, 3000);

                    }
                } else {
                    showGPSDisabledAlertToUser();
                }

            } catch (Exception e) {
                Log.e("error-->>", e.toString());

            }


        } else {


            gpstracker = new GPSTracker(this);


            if (gpstracker.canGetLocation()) {
                currentLatLong = new LatLng(gpstracker.latitude, gpstracker.longitude);
                if (mMap != null && (currentLatLong.latitude != 0 || currentLatLong.longitude != 0)) {
                    currentLocation.remove();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 16));
                    currentLocation = mMap.addMarker(new MarkerOptions().position(currentLatLong).title("you are here!!"));
                }
                try {
                    waitingprogress.dismiss();
                } catch (Exception ex) {
//                        Toast.makeText(getActivity(), "GPS Dialog" + ex.toString(), Toast.LENGTH_SHORT).show();
                }

                return;
            }
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    waitingforgps();
                }
            }, 3000);
        }


    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(movementlocation, 18));
        currentLocation = mMap.addMarker(new MarkerOptions().position(movementlocation).title("you are here!!"));

        final UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
        fetchparkings();


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            waitingforgps();
        } else {
            if (isLocationNil())
                showGPSDisabledAlertToUser();
        }


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        parkinglayout.setVisibility(View.GONE);
        String[] parking_count = marker.getTag().toString().split(",");

        two_wheeeler_count = Integer.parseInt(parking_count[0]);
        four_wheeler_count = Integer.parseInt(parking_count[1]);

        Log.d("COUNT", two_wheeeler_count + " " + four_wheeler_count);
        if (two_wheeeler_count <= 0 && four_wheeler_count <= 0) {
            book.setVisibility(View.GONE);
        } else if (two_wheeeler_count <= 0 || four_wheeler_count <= 0) {
            book.setVisibility(View.VISIBLE);

        }



        if (currentLocation != null) {
            if (!marker.getId().equalsIgnoreCase(currentLocation.getId())) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.focusparking));
                if (previousSelectedMarker != null) {
                    previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.parking));
                }
                previousSelectedMarker = marker;
                for (int i = 0; i < parkingList.size(); i++) {
                    if (parkingList.get(i).getMarkerID().equalsIgnoreCase(marker.getId())) {
                        parkData = parkingList.get(i);
                        parkinglayout.setVisibility(View.VISIBLE);
                        assignPopValue();
                    }
                }
            }
        }
        return false;
    }

    private void assignPopValue() {
        TextView tvTitleAddress = (TextView) parkinglayout.findViewById(R.id.textViewTitleAddress);
        TextView subTitleAddress = (TextView) parkinglayout.findViewById(R.id.subTitleAddress);
        TextView txtprice = (TextView) parkinglayout.findViewById(R.id.txtprice);


        TextView distanceFrom = (TextView) parkinglayout.findViewById(R.id.distanceFrom);
        ImageView parkImage = (ImageView) parkinglayout.findViewById(R.id.parkImage);
        Glide.with(this).load("http://www.championparkingnyc.com/images/BigStock/116212907[1].jpg").into(parkImage);
        tvTitleAddress.setText(parkData.getS_name() + "-" + parkData.getS_managername());
        subTitleAddress.setText(parkData.getS_address());
        txtprice.setText("2w :" + parkData.getS_2wheel() + "  ,4w : " + parkData.getS_4wheel());
        if (isLocationNil()) {
            distanceFrom.setText("few Km");
        } else {
            try {
                distanceFrom.setText("" + distance(Double.parseDouble(parkData.getS_lati()),
                        Double.parseDouble(parkData.getS_longi()),
                        currentLatLong.latitude,
                        currentLatLong.longitude));
            } catch (Exception ex) {
                distanceFrom.setText("few Km");
            }
        }

    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    private void getPlace(String url, final AutoCompleteTextView tmptextview) {

        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {


                if (json != null) {

                    Jclass jsonn;

                    JsonElement json2 = new JsonParser()
                            .parse(json.toString());

                    Type type = new TypeToken<Jclass>() {
                    }.getType();
                    jsonn = new Gson().fromJson(json2,
                            type);


                    if (jsonn.getStatus().equalsIgnoreCase("OK")) {

                        List<String> placeName = new ArrayList<String>();


                        for (int i = 0; i < jsonn.getPredictions().size(); i++) {
                            placeName.add(jsonn.getPredictions().get(i).getDescription());
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchParkingActivity.this,
                                android.R.layout.simple_dropdown_item_1line, placeName);


                        tmptextview.setAdapter(adapter);
                        String tmptxt = tmptextview.getText().toString();


                        int pos = 0;
                        for (int i = 0; i < jsonn.getPredictions().size(); i++) {
                            if (jsonn.getPredictions().get(i).getDescription().equalsIgnoreCase(tmptxt)) {
                                pos = i;

                            }

                        }
                        getLatlong("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + jsonn.getPredictions().get(pos).getPlace_id() + "&key=" + PLACE_API_Key);
                    }
                } else {
                    Toast.makeText(aq.getContext(), "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void getLatlong(String url) {

        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {


                if (json != null) {

                    Jclass jsonn = null;

                    jsonn = new Jclass();

                    JsonElement json2 = new JsonParser()
                            .parse(json.toString());

                    Type type = new TypeToken<Jclass>() {
                    }.getType();
                    jsonn = new Gson().fromJson(json2,
                            type);


                    if (jsonn.getStatus().equalsIgnoreCase("OK")) {

                        try {


                            for (int i = 0; i < json.length(); i++) {
                                JSONObject jsonobject = json.getJSONObject("result");
                                JSONObject jsonobject2 = jsonobject.getJSONObject("geometry");
                                locationObj = jsonobject2.getJSONObject("location");


                            }

                        } catch (Exception e) {

                            Toast.makeText(aq.getContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                } else {
                    Toast.makeText(aq.getContext(), "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String getAutoCompleteUrl(String place) {

        String key = "key=" + PLACE_API_Key;

// place to be be searched
        String input = "input=" + place;

// place type to be searched
        String types = "types=geocode";


        String sensor = "sensor=false";

// Building the parameters to the web service
        String parameters = input + "&" + types + "&" + sensor + "&" + key;


// Output format
        String output = "json";

// Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

        return url;
    }

    void hideKeybord() {
        try {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_address.getWindowToken(), 0);
        } catch (Exception e) {

        }
    }


    public void fetchparkings()

    {
        selected_date = TimeSlotActivity.slot_date;
        selected_time = TimeSlotActivity.slot_time;

        HashMap<String, String> paramaters = new HashMap<>();
        paramaters.put("parking_date", selected_date);
        paramaters.put("parking_time", selected_time);


        String url = "http://parkmycars.co.in/parkmycar/webservices/fetch/fetchparkings";

        NetworkUtils.sendVolleyPostRequest(url, new NetworkUtils.VolleyCallbackString() {
            @Override
            public void onSuccess(String result) {


                JSONObject mainObject = null;
                try {
                    mainObject = new JSONObject(result);
                    JSONArray spaces = mainObject.getJSONArray("spaces");

                    Type type = new TypeToken<ArrayList<ParkData>>() {
                    }.getType();
                    parkingList = new Gson().fromJson(String.valueOf(spaces.toString()), type);


                    if (parkingList == null) {
                        parkingList = new ArrayList<>();
                    }

                    if (parkingList.size() > 0) {
                        for (int i = 0; i < parkingList.size(); i++) {

                            JSONObject feedObj = (JSONObject) spaces.get(i);
                            Double lat = Double.parseDouble(parkingList.get(i).getS_lati());
                            Double lng = Double.parseDouble(parkingList.get(i).getS_longi());

                            int two_wheeler_total = Integer.parseInt(feedObj.getString("S_2totalslot"));
                            int four_wheeler_total = Integer.parseInt(feedObj.getString("S_4totalslot"));
                            int four_wheeler_booked = Integer.parseInt(feedObj.getString("TotalBooked_4"));
                            int two_wheeler_booked = Integer.parseInt(feedObj.getString("TotalBooked_2"));

                            String s1 = "Total";

                            int two_wheeler_available = two_wheeler_total - two_wheeler_booked;
                            int four_wheeler_available = four_wheeler_total - four_wheeler_booked;




                            if (two_wheeler_available <= 0 && four_wheeler_available <= 0) {
                                Log.d("error", s1);
                                s1 = "parking full";

                            } else if (two_wheeler_available <= 0) {
                                s1 = "2-wheeler full" + "\n" + "4-wheeler:" + four_wheeler_available+"available";

                            } else if (four_wheeler_available <= 0) {
                                s1 = "2-wheeler:" + two_wheeler_available +"available" + "\n" + "4-wheeler full";

                            } else {
                                s1 = "2-wheeler:" + two_wheeler_available +"available"+ "\n" + "4-wheeler:" + four_wheeler_available+"available";

                            }


                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                    .title(parkingList.get(i).getS_name())
                                    .snippet("2 wheel: " + Integer.parseInt(feedObj.getString("S_2wheel")) + " Rs." + "\n" + "4 Wheel: " + Integer.parseInt(feedObj.getString("S_4wheel")) + " Rs. \n" + s1)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)));

                            marker.setTag(two_wheeler_available + "," + four_wheeler_available);
                            parkingList.get(i).setMarkerID(marker.getId());

                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {

                                    Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                                    LinearLayout info = new LinearLayout(context);
                                    info.setOrientation(LinearLayout.VERTICAL);

                                    TextView title = new TextView(context);
                                    title.setTextColor(Color.BLACK);
                                    title.setGravity(Gravity.CENTER);
                                    title.setTypeface(null, Typeface.BOLD);
                                    title.setText(marker.getTitle());

                                    TextView snippet = new TextView(context);
                                    snippet.setTextColor(Color.GRAY);
                                    snippet.setText(marker.getSnippet());

                                    info.addView(title);
                                    info.addView(snippet);

                                    return info;
                                }
                            });

                        }
                    } else {
                        nolocationlayout.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {

            }
        }, this, paramaters);
    }


}