package com.aguileda.myopka;


import android.content.Context;
import android.widget.Toast;
import com.aguileda.myopka.SendHttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Karotz API
 * Implemented method:
 *      make the rabbit talk
 */
public class KarotzInterface {

    private String kIP = "192.168.0.19";
    private Context context;
    public KarotzInterface(Context context){
        this.context = context;

    }

    public void saySomething(String text) {

        try {
            text = URLEncoder.encode(text, "UTF-8");
        }
        catch (UnsupportedEncodingException e){
            Toast.makeText(context,"unsupported character set",Toast.LENGTH_SHORT).show();
        }

        String URL = "http://" + kIP + "/cgi-bin/tts?voice=alice&text="+text+"&nocache=0";


        try {
            new SendHttpRequest(context).execute(URL);


        } catch (Exception ex) {
            Toast.makeText(context,"Fail!",Toast.LENGTH_SHORT).show();
        }

    }
}

