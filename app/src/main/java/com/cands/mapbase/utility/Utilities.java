package com.cands.mapbase.utility;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

import com.cands.mapbase.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Yakaswamy.g on 4/12/2016.
 */
public class Utilities {

//    public static final String APP_URL = "http://www.taxiclando.com/";
    public static final String APP_URL = "https://rokelbank.sl/riderapp/";
    private static ProgressDialog dialog;

    public static void setUserOInfo(Context context, String name, String number, String vehicleNo, String email, String password) {
        SharedPreferences example = context.getSharedPreferences("map", 0);
        SharedPreferences.Editor editor = example.edit();
        editor.putString("name", name);
        editor.putString("number", number);
        editor.putString("vehicleNo", vehicleNo);
        editor.putString("email", email);
        editor.putString("password", password);

        editor.commit();
    }

    public static String getLoginUserName(Context context) {
        SharedPreferences example = context.getSharedPreferences("map", 0);
        String name = example.getString("name", null);
        return name;
    }

    /**
     * method will convert the Input stream to String
     *
     * @param is
     * @return String
     * @throws IOException
     * @throws Exception
     */
    public static String convertStreamToString(InputStream is)
            throws IOException, Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        reader.close();
        System.out.println("String from Server===" + sb.toString());
        return sb.toString();
    }

    /**
     * Checking the network Availability
     *
     * @return ture if network is availability in device
     */
    public static Boolean networkAvailability(Context c) {
        try {
            boolean wifiAvailability = false;
            boolean gprsAvailability = false;

            ConnectivityManager cManager = (ConnectivityManager) c
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfo = new NetworkInfo[0];
            if (cManager != null) {
                networkInfo = cManager.getAllNetworkInfo();
            }

            for (NetworkInfo nInfo : networkInfo) {
                if (nInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                    if (nInfo.isConnected())
                        wifiAvailability = true;
                }
                if (nInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                    if (nInfo.isConnected())
                        gprsAvailability = true;
                }
            }

            return wifiAvailability || gprsAvailability;
        } catch (Exception e) {
            Log.v("networkAvailability:", e.toString());
        }

        return false;
    }

    public static void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    public static void showProgressDialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage(context.getResources().getString(R.string.progress_pleaseWait));
        dialog.show();
    }

    public static void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private static void setDeviceId(String deviceId, Context context) {
        SharedPreferences example = context.getSharedPreferences("deviceId", 0);
        SharedPreferences.Editor editor = example.edit();
        editor.putString("deviceId", deviceId);
        editor.apply();
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {

        SharedPreferences example = context.getSharedPreferences("deviceId", 0);
        String number = example.getString("deviceId", null);
        if (number == null) {
            number = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            setDeviceId(number, context);
        }
        return number;
    }
}
