package com.example.pinalmeruliya.myapplication.helper;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class CommonFunction
{
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {

         //   showAlertDialogForInternet(context, "No internet connection", "You do not have internet connection connect and try");


            return false;
        } else
            return true;// return isInternetAvailable(context);
    }
  /*  public static void showAlertDialogForInternet(final Context context, String title, String message) {
        try {
            final Dialog introDialog = new Dialog(context);
            introDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            introDialog.setContentView(R.layout.custom_dialog);
            introDialog.setCancelable(false);
            introDialog.setCanceledOnTouchOutside(false);
            Window window = introDialog.getWindow();
            window.setBackgroundDrawableResource(android.R.color.transparent);
            TextView dialogTitle = (TextView) window.findViewById(R.id.textviewTitle);
            TextView dialogMessage = (TextView) window.findViewById(R.id.textviewMessage);
            Button okButton = (Button) window.findViewById(R.id.buttonAccept);
            //okButton.setText("Okay");
            Button buttonDecline = (Button) window.findViewById(R.id.buttonDecline);
            //buttonDecline.setVisibility(View.GONE);
            dialogTitle.setText(title);
            dialogMessage.setText(message);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    introDialog.dismiss();

                }
            });
            introDialog.show();
        } catch (Exception e) {
            Log.e("error-->>", e.getMessage());
        }
    }*/
}
