package com.cands.mapbase.asynctask;

/**
 * Created by Swamy on 3/4/2016.
 */

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.cands.mapbase.listener.AsyncTaskCompleteListener;
import com.cands.mapbase.utility.Utilities;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


/**
 * Created by Yakaswamy.g on 2/22/2016.
 */

public class MyAsyncTask extends AsyncTask<String, Void, String> {

    private String jsonData;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;
    private int connectionTimeout;
    private int socketTimeout;

    public MyAsyncTask(String requestData, AsyncTaskCompleteListener asyncTaskCompleteListener, int connectionTimeout, int socketTimeout) {
        jsonData = requestData;
        Log.v("","jsonData"+jsonData);
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
        this.connectionTimeout = connectionTimeout;
        this.socketTimeout = socketTimeout;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @SuppressLint("NewApi")
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpURLConnection = null;
        HttpURLConnection http_conn=null;
        try {
//            URL myurl=new URL(params[0]);
//            httpURLConnection = (HttpURLConnection) myurl.openConnection();
//            System.out.println("code:"+httpURLConnection.getResponseCode());
//
//            //setting properties to httpURLConnection
//            httpURLConnection.setConnectTimeout(connectionTimeout);
//            httpURLConnection.setReadTimeout(socketTimeout);
//            httpURLConnection.setRequestMethod("POST");
//            httpURLConnection.setDoInput(true);
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//
//            OutputStream os = httpURLConnection.getOutputStream();
//            os.write(jsonData.getBytes(StandardCharsets.UTF_8));
//            os.close();
//
//            //setting request data to httpURLConnection
//            /*OutputStream outputStream = httpURLConnection.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//            writer.write(jsonData);
//            writer.flush();
//            writer.close();*/
//
//
//            Log.i("AsyncTask", "url :" + params[0]);
//          //  Log.i("AsyncTask", "url1 :" + params[1]);
//            Log.i("AsyncTask", "request data :" + jsonData);
//
//            InputStream inputStream = httpURLConnection.getInputStream();
//            String responseStr = Utilities.convertStreamToString(inputStream);
//            Log.v("","responseStr"+responseStr);




            HttpURLConnection.setFollowRedirects(true); // defaults to true


            URL request_url = new URL(params[0]);
            http_conn = (HttpURLConnection)request_url.openConnection();
            http_conn.setConnectTimeout(connectionTimeout);
            http_conn.setReadTimeout(socketTimeout);
            http_conn.setInstanceFollowRedirects(true);
            http_conn.setDoOutput(true);
            PrintWriter out = new PrintWriter(http_conn.getOutputStream());
            if (jsonData != null) {
                out.println(jsonData);
            }
            out.close();
            System.out.println(http_conn.getResponseCode());
//            return responseStr;
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (http_conn != null)
                http_conn.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        asyncTaskCompleteListener.onAsynComplete(result);
    }
}


