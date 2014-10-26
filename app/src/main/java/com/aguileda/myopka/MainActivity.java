package com.aguileda.myopka;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.aguileda.myopka.KarotzInterface;


public class MainActivity extends Activity {

    private Button sendButton;
    private Button clearButton;
    private KarotzInterface myKarotz;
    private Spinner voiceSpinner;
    private EditText text;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myKarotz = new KarotzInterface(this);

        text = (EditText) findViewById(R.id.editText);

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
                Toast.makeText(MainActivity.this,String.valueOf(MainActivity.this.voiceSpinner.getSelectedItemPosition()),Toast.LENGTH_SHORT).show();

                MainActivity.this.myKarotz.saySomething(textToSay,voice);
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
    }

    private void loadState(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String savedText = sharedPreferences.getString("current_text", "");
        int savedVoiceIndex = sharedPreferences.getInt("current_voice", 0);

        text.setText(savedText);
        voiceSpinner.setSelection(savedVoiceIndex);
    }

    private void saveState(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("current_voice", voiceSpinner.getSelectedItemPosition());
        ed.putString("current_text",text.getText().toString());
        ed.apply();

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
