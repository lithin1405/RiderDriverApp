package com.cands.mapbase;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cands.mapbase.asynctask.MyAsyncTask;
import com.cands.mapbase.listener.AsyncTaskCompleteListener;
import com.cands.mapbase.utility.Utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener,LocationListener {

    private EditText edtName, edtMobileNo, edtVehicleNo, edtPassword, edtCnfmPassword, edtEmail;

    private Button btnSubmit;

    private Context context;
    private LocationManager locationManager;
    private boolean send = true;
    private String currentLocation;
//    private static final String REGISTER_URL = "http://www.taxiclando.com/driverregister.php?";
    private static final String REGISTER_URL = "https://rokelbank.sl/riderapp/rider_register.php?";
    private RadioGroup vehicleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Utilities.getLoginUserName(this) != null){
            navigateToMain();
        }
        setContentView(R.layout.activity_registration);

        context = this;

        edtName = (EditText) findViewById(R.id.reg_name);
        edtMobileNo = (EditText) findViewById(R.id.reg_mobileNo);
        edtVehicleNo = (EditText) findViewById(R.id.reg_vehicleNo);
        edtEmail = (EditText) findViewById(R.id.reg_emailId);
        edtPassword = (EditText) findViewById(R.id.reg_password);
        edtCnfmPassword = (EditText) findViewById(R.id.reg_cnfmPassword);

        btnSubmit = (Button) findViewById(R.id.reg_submit);

        vehicleType = (RadioGroup) findViewById(R.id.vehicleType);

        btnSubmit.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);


    }

    @Override
    public void onClick(View v) {
//        String validation = validateFields();
//        if(validation != null)
//        {
//            Utilities.showAlert(this,"Alert", validation);
//            return;
//        }
        if (Utilities.networkAvailability(context)) {
//                        showProgress();
            if(currentLocation != null) {
                registrationProcess();
            }else{
                Toast.makeText(context, "Wait till you get location", Toast.LENGTH_SHORT).show();
            }
            //loginProcess();
        } else {
            Utilities.showAlert(context, null, getString(R.string.check_connection));
        }
        //navigateToMain();
       // Toast.makeText(getApplicationContext(),"Thank you registering with us",Toast.LENGTH_SHORT).show();
        Utilities.setUserOInfo(context, getText(edtName), getText(edtMobileNo), getText(edtVehicleNo), getText(edtEmail), getText(edtPassword));
        navigateToMain();
    }

    private String validateFields() {
        if(edtEmail.getText().toString().trim().equals("") && edtPassword.getText().toString().trim().equals(""))
            return null;
        else
            return "Enter valid mail ID or Password";

    }

    private void navigateToMain(){

//        Utilities.showAlert(context, "", "Thank you registering with us");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void registrationProcess() {

        String fname = edtName.getText().toString().trim().toLowerCase();
        String fmobileno = edtMobileNo.getText().toString().trim().toLowerCase();
        String fvehicleno = edtVehicleNo.getText().toString().trim().toLowerCase();
        String femail = edtEmail.getText().toString().trim().toLowerCase();
        String fpassword = edtPassword.getText().toString().trim().toLowerCase();
        String flocation=String.valueOf(currentLocation);
        String fvehicle_type = "";
        String deviceid="";
        if(vehicleType.getCheckedRadioButtonId() == R.id.car){
            fvehicle_type+="car";
        }else if(vehicleType.getCheckedRadioButtonId() == R.id.clado){
            fvehicle_type+="bike";
        }else if(vehicleType.getCheckedRadioButtonId() == R.id.keke){
            fvehicle_type+="keke";
        }
        deviceid+=""+Utilities.getDeviceId(context);
        Log.v("","location"+flocation);
        Log.v("","VType"+fvehicle_type);
        Log.v("","deviceid"+deviceid);
        register(fname,fmobileno,fvehicleno,femail,fpassword,flocation,fvehicle_type,deviceid);


//        String url = "http://www.taxiclando.com/driverregister.php?"+ getData();
//        MyAsyncTask loginAsync = new MyAsyncTask("", completeListener, 30*1000, 30*1000);
//        Utilities.showProgressDialog(context);
//        loginAsync.execute(url);

    }

    private void register(String fname, String lname, String mail, String phno, String pass, String location, String data, String deviceid) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegistrationActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
              //  loading.dismiss();
               // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("name",params[0]);
                data.put("mobileno",params[1]);
                data.put("vehicleno",params[2]);
                data.put("email",params[3]);
                data.put("password",params[4]);
                data.put("location",params[5]);
                data.put("vehicle_type",params[6]);
                data.put("device_id",params[7]);
//                if(vehicleType.getCheckedRadioButtonId() == R.id.cab){
//                   // data+="&type=cab";
//                    data.put("type=cab",params[5]);
//                }else if(vehicleType.getCheckedRadioButtonId() == R.id.clado){
//                    //data+="&type=clando";
//                    data.put("type=clando",params[6]);
//                }
//                getData();

                String result = ruc.sendPostRequest(REGISTER_URL,data);
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();

              //  Toast.makeText(getApplicationContext(),"Thank For Registering With us",Toast.LENGTH_SHORT).show();

                return  result;
            }


        }

        RegisterUser ru = new RegisterUser();
        ru.execute(fname, lname,mail,phno,pass,location,data,deviceid);
    }

    private String getData(){
        String data = "";
        //name=swamy&mobilenumber=9133455341&vehiclenumber=4823&emailid=swamy@gmail.com&password=ser&deviceid=11123&location=hyd
//        data+="name="+getText(edtName);
//        data+="&number="+getText(edtMobileNo);
//        data+="&vehicleNo="+getText(edtVehicleNo);
//        data+="&email="+getText(edtEmail);
//        data+="&password="+getText(edtPassword);
        data+="&device_id="+Utilities.getDeviceId(context);
        data+="&location="+currentLocation;

        if(vehicleType.getCheckedRadioButtonId() == R.id.car){
            data+="&type=car";
        }else if(vehicleType.getCheckedRadioButtonId() == R.id.clado){
            data+="&type=bike";
        }else if(vehicleType.getCheckedRadioButtonId() == R.id.keke){
            data+="&type=keke";
        }
        Utilities.showAlert(context, "", "Thank you registering with us");
        return data;
    }

    private AsyncTaskCompleteListener completeListener = new AsyncTaskCompleteListener() {
        @Override
        public void onAsynComplete(String result) {
            if (result != null) {
                Utilities.showAlert(context, "", "Thank you registering with us");
                Utilities.setUserOInfo(context, getText(edtName), getText(edtMobileNo), getText(edtVehicleNo), getText(edtEmail), getText(edtPassword));
                navigateToMain();
            } else {
                Utilities.showAlert(context, "Network problem", "Check your internet connection");
            }
            Utilities.hideProgressDialog();
        }
    };

    private String getText(EditText edt){
        return edt.getText().toString().trim();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            if(send){
                send = false;
                currentLocation = location.getLatitude()+","+location.getLongitude();
                Toast.makeText(context, "Got location submit now", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class RegisterUserClass {

        public String sendPostRequest(String requestURL,
                                      HashMap<String, String> postDataParams) {

            URL url;
            String response = "";
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    response = br.readLine();
                }
                else {
                    response="Error Registering";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }
    }
}
