package com.aguileda.myopka;

/**
 * Class for sending http request
 * Usage: new SendHttpRequest(context).execute(URL);
 * context: Context
 * URL: String
 */


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

public class SendHttpRequest extends AsyncTask<String,Void,String>{

    private Context context;
    public SendHttpRequest(Context context) {
        this.context = context;

    }

    //check Internet connection.
    private Boolean checkInternetConnection(){
        ConnectivityManager check = (ConnectivityManager) this.context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (check != null)
        {
            NetworkInfo[] info = check.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i <info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        else{
            Toast.makeText(context, "not connected to internet",
                    Toast.LENGTH_SHORT).show();

        }
        return false;
    }
    protected void onPreExecute(){
        checkInternetConnection();
    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            String link = arg0[0];
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader
                    (is, "UTF-8") );
            String data;
            String webPage = "";
            while ((data = reader.readLine()) != null){
                webPage += data + "\n";
            }
            return webPage;
        }catch(Exception e){
            return "Exception: " + e.getMessage();
        }
    }
    /*@Override
    protected void onPostExecute(String result){
        Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
    }*/

}