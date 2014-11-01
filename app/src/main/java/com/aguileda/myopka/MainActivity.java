package com.aguileda.myopka;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.aguileda.myopka.KarotzInterface;
import com.aguileda.myopka.util.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity {

    private Button sendButton;
    private Button clearButton;
    private KarotzInterface myKarotz;
    private Spinner voiceSpinner;
    private AutoCompleteTextView text;
    private HistoryAdapter adapterH;
    private ArrayList<String> historyList;
    private TextView debugText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myKarotz = new KarotzInterface(this);

        debugText = (TextView) findViewById(R.id.debugText);
        text = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        if (historyList == null){
            historyList = new ArrayList<String>();
        }




        voiceSpinner = (Spinner) findViewById(R.id.voiceSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.voices_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        voiceSpinner.setAdapter(adapter);

        sendButton = (Button) findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textToSay = MainActivity.this.text.getText().toString();

                String voice = MainActivity.this.voiceSpinner.getSelectedItem().toString();

                MainActivity.this.myKarotz.saySomething(textToSay,voice);

                addItemToHistory(textToSay);

                MainActivity.this.text.setText("");



            }
        });

        clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.text.setText("");
            }
        });

        loadState();

        adapterH = new HistoryAdapter
                (this,R.layout.history_item,historyList);

        adapterH.setNotifyOnChange(true);
        text.setAdapter(adapterH);
    }

    private void loadState(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        int savedVoiceIndex = sharedPreferences.getInt("current_voice", 0);

        try {
            historyList = (ArrayList<String>) ObjectSerializer.
                    deserialize(sharedPreferences.getString("history",
                            ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }


        voiceSpinner.setSelection(savedVoiceIndex);
        myKarotz.setKIp("192.168.0.19");
    }

    private void saveState(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("current_voice", voiceSpinner.getSelectedItemPosition());

        try {
            ed.putString("history", ObjectSerializer.serialize(historyList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ed.apply();

    }

    public void setDebugText(){
        debugText.setText(String.valueOf(historyList));
    }

    public void addItemToHistory(String data) {

        if (!historyList.contains(data)) {

            historyList.add(data);
            adapterH = new HistoryAdapter
                    (MainActivity.this, R.layout.history_item, historyList);
            text.setAdapter(adapterH);
        }
    }


    public void removeItemFromHistory(int index){

        historyList.remove(index);
        adapterH = new HistoryAdapter
                (MainActivity.this, R.layout.history_item, historyList);
        text.setAdapter(adapterH);
        text.dismissDropDown();
        text.setText("");

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
