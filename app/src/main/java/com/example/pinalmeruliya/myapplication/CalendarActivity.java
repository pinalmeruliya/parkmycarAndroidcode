package com.example.pinalmeruliya.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.Calendar;
import android.view.View;

import com.example.pinalmeruliya.myapplication.helper.PayPalConfig;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
//import android.icu.math.BigDecimal;
import android.app.Activity;
import android.util.Log;
import org.json.*;

import java.math.BigDecimal;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.example.pinalmeruliya.myapplication.helper.SelectDateFragment;
import com.example.pinalmeruliya.myapplication.helper.TimePickerFragment;
public class CalendarActivity extends AppCompatActivity {

    private TextView charge;
    private Button button;
    private DatePicker datePicker;
    Context context;
    RadioGroup radioGroup;
    RadioButton radioButtonFour;
    RadioButton radioButtonTwo;
    public static String amount;


    //Payment Amount
    private String paymentAmount;
    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;
    public static String selected_4charge_selection;
    public static String selected_2charge_selection;
    public static int two_wheeeler_count_selection;
    public static int four_wheeeler_count_selection;

    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        context=this;

        selected_4charge_selection = BookingDetailsActivity.selected_4charge;
        selected_2charge_selection = BookingDetailsActivity.selected_2charge;
        two_wheeeler_count_selection = SearchParkingActivity.two_wheeeler_count;
        four_wheeeler_count_selection = SearchParkingActivity.four_wheeler_count;

        charge = (TextView) findViewById(R.id.textView_charge);

        radioGroup =(RadioGroup)findViewById(R.id.radioGroup);
        radioButtonFour=(RadioButton)findViewById(R.id.radioButton);
        radioButtonTwo=(RadioButton)findViewById(R.id.radioButton2);

        button=(Button)findViewById(R.id.Select_date);
        Log.d("error",selected_2charge_selection+" " +selected_4charge_selection+" " +two_wheeeler_count_selection +" "+four_wheeeler_count_selection);

        if(two_wheeeler_count_selection<=0)
        {
            radioButtonTwo.setEnabled(false);

            Log.d("error",selected_2charge_selection+"loop 1");
        }
        else if(four_wheeeler_count_selection<=0)
        {
            radioButtonFour.setEnabled(false);
            Log.d("error",selected_2charge_selection+"loop 2");
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {



                switch (checkedId) {
                    case R.id.radioButton:
                        Log.d("error",selected_4charge_selection+"case 1");

                        charge.setText("Your payable amount is "+selected_4charge_selection+"Rs.");
                        amount = selected_4charge_selection;
                        break;
                    case R.id.radioButton2:


                        charge.setText("Your payable amount is "+selected_2charge_selection+"Rs.");
                        amount =selected_2charge_selection;
                        break;
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getPayment();

            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getPayment() {
        //Getting the amount from editText
        paymentAmount =  amount;

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "your payment amount", PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

}







