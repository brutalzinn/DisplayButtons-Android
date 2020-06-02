package net.nickac.buttondeck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.nickac.buttondeck.utils.Constants;

public class ConfigActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String SWITCH1 = "switch1";

    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_config_app);


        editText = (EditText) findViewById(R.id.config_port_text);

        Button config_button_save = findViewById(R.id.config_save);
     Button config_button_back = findViewById(R.id.config_back);
     config_button_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.d("Debug", "Salvando nova porta");
             //   setContentView(R.layout.activity_config_app);
                saveData();
            }
        });

     config_button_back.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {

             Log.d("Debug", "Voltando");

             Intent intent = new Intent(getApplicationContext(), MainActivity.class);
             //    intent.putExtra(ButtonDeckActivity.EXTRA_MODE, "1");
             startActivity(intent);
         //    setContentView(R.layout.activity_principal_menu);

         }
     });
        loadData();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, editText.getText().toString());
      //editor.putBoolean(SWITCH1, switch1.isChecked());
        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editText.setText(sharedPreferences.getString(TEXT, ""));
       // switchOnOff = sharedPreferences.getBoolean(SWITCH1, false);
    }

}
