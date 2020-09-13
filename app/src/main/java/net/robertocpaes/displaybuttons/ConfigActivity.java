package net.robertocpaes.displaybuttons;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import net.robertocpaes.displaybuttons.utils.Admob;
import net.robertocpaes.displaybuttons.utils.Constants;
import net.robertocpaes.displaybuttons.utils.MySession;

import java.util.Arrays;
import java.util.List;

public class ConfigActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String SWITCH1 = "switch1";
    private BillingProcessor bp;
    private MySession session;
    private Admob AdMobBanner ;
    private TextView textView;
    private EditText editText;
    private FrameLayout adContainerView;
    private AdView adView;
    private Activity mCurrentActivity = null;
    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }
    private boolean readyToPurchase = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_config_app);


        AdMobBanner= new Admob();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
                        .build());
        List<String> testDeviceIds = Arrays.asList("DF163BC1F66E309840CFFE4E9DF6BCC4");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        adContainerView = findViewById(R.id.ad_view_container_config);
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                Activity currentActivity = ((ConfigActivity)Constants.ConfigActivityContext).getCurrentActivity();

                AdMobBanner.loadBanner(adContainerView,Constants.ConfigActivityContext,currentActivity,"ca-app-pub-2620537343731622/3268521006");
            }
        });

        // Create an ad request.


        // Start loading the ad in the background.




        editText = (EditText) findViewById(R.id.config_port_text);

        Button config_button_save = findViewById(R.id.config_save);
     Button config_button_back = findViewById(R.id.config_back);
//        Button paypremium_button = findViewById(R.id.buyproversion);
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

      /*//  paypremium_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              bp.purchase((Activity) getApplicationContext(),"PremiumProduct");
            }
        });*/
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
    @Override
    public void onPause() {
        AdMobBanner.onPause();
        clearReferences();

        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        Constants.ConfigActivityContext = this;
        Constants.ConfigActivityContext.setCurrentActivity(this);

        AdMobBanner.OnResume();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        AdMobBanner.OnDestroy();
        clearReferences();

        super.onDestroy();
    }
    private void clearReferences(){
        Activity currActivity =  Constants.ConfigActivityContext.getCurrentActivity();
        if (this.equals(currActivity))
            Constants.ConfigActivityContext.setCurrentActivity(null);
    }
}
