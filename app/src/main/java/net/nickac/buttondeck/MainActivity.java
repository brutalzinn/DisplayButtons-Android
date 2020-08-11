package net.nickac.buttondeck;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import net.nickac.buttondeck.networking.io.SocketServer;
import net.nickac.buttondeck.utils.Constants;
import net.nickac.buttondeck.utils.networkscan.NetworkDevice;
import net.nickac.buttondeck.utils.networkscan.NetworkDeviceAdapter;
import net.nickac.buttondeck.utils.networkscan.NetworkSearch;

import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.println;

public class MainActivity extends AppCompatActivity {
   public String autoScanPref = "didAutoScan";
    public static final String EXTRA_MODE = "0";
    public static String mode_init = "0";
    public static String mode_init_ip = "127.0.0.1";
    private InterstitialAd mInterstitialAd;

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    @Override
    public void onBackPressed() {
        getPreferences(MODE_PRIVATE).edit().putBoolean(autoScanPref, false).apply();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admob);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                setContentView(R.layout.activity_principal_menu);
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2620537343731622~6773279064");
        Button config_button = findViewById(R.id.main_action_config);
        config_button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfigActivity.class);
            //    intent.putExtra(ButtonDeckActivity.EXTRA_MODE, "1");
                startActivity(intent);
              //  setContentView(R.layout.activity_config_app);
            }
        });
        View.OnClickListener actionHandle = null;

//        rescanButton.setOnClickListener(view -> scanDevices());

        String mode = getIntent().getStringExtra("mode");
try {
    if (mode.length() > 0) {

        Intent intent = new Intent(getApplicationContext(), ButtonDeckActivity.class);
        intent.putExtra(ButtonDeckActivity.EXTRA_MODE, "1");
        startActivity(intent);
        mode_init = "1";
        Log.d("debug", "ENTRANDO NO MODO USB AUTOMATICAMENTE.. " + mode);
    }

}catch (Exception e){
    println("DEU ERRO AQUI. MAS PASSO BEM");

}



        actionHandle = v -> {
            switch (v.getId()) {
                case R.id.button_socket:
                    setContentView(R.layout.activity_main);
                    Log.d("DEBUG","CALLED SOCKET WIFI");


                    TextView textView = findViewById(R.id.protocolVersionTextView);
                    textView.setText(textView.getText().toString().replace("{0}", String.valueOf(Constants.PROTOCOL_VERSION)));

                    Button rescanButton = findViewById(R.id.rescanButton);

                    boolean alreadyScanned = getPreferences(MODE_PRIVATE).getBoolean(autoScanPref, false);
                    rescanButton.setVisibility(!alreadyScanned ? View.INVISIBLE : View.VISIBLE);
                    rescanButton.setOnClickListener(view -> scanDevices());
                    mode_init = "0";
                   if (!alreadyScanned) {




                        scanDevices();

                    }

                    break;

                case R.id.button_usb:
                    Intent intent = new Intent(getApplicationContext(), ButtonDeckActivity.class);
                //    intent.putExtra(ButtonDeckActivity.EXTRA_MODE, 1);
                    intent.putExtra(ButtonDeckActivity.EXTRA_MODE, "1");
                    mode_init = "1";
                    startActivity(intent);


                    break;
            }
        };

        (findViewById(R.id.button_usb)).setOnClickListener(actionHandle);

        (findViewById(R.id.button_socket)).setOnClickListener(actionHandle);
        //    devices.
        // Toast.makeText(this, "Connecting to " + devices.get(0).getDeviceName() + "!", Toast.LENGTH_LONG).show();
        //   Toast.makeText(getApplicationContext(), "Connecting por usb !", Toast.LENGTH_LONG).show();
        // intent.putExtra(ButtonDeckActivity.EXTRA_IP, "127.0.0.1");
        //               startActivity(intent);


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void scanDevices() {
        if (isEmulator()) {
            // Emulator
            Intent intent = new Intent(this, ButtonDeckActivity.class);
            intent.putExtra(ButtonDeckActivity.EXTRA_IP, "10.0.2.2");
            startActivity(intent);
            return;
        }

        Button rescanButton = findViewById(R.id.rescanButton);

        //Hide the button for now.
        rescanButton.setVisibility(View.INVISIBLE);


        NetworkDeviceAdapter adapter = new NetworkDeviceAdapter(this, new ArrayList<>());
        NetworkSearch.AsyncScan scan = new NetworkSearch.AsyncScan(adapter);
        scan.execute(adapter);

        scan.setAfterCompletion(() -> {
            List<NetworkDevice> devices = adapter.getDevices();
            switch (devices.size()) {
                case 0:
                    TextView textView = findViewById(R.id.statusTextView);
                    textView.setText(getString(R.string.devices_found_none));
                    break;
                case 1:
                    Toast.makeText(this, "Connecting to " + devices.get(0).getDeviceName() + "!", Toast.LENGTH_LONG).show();

                    //Connect to the device
                    Intent intent = new Intent(this, ButtonDeckActivity.class);
                    intent.putExtra(ButtonDeckActivity.EXTRA_IP, devices.get(0).getIp());
                    mode_init_ip = devices.get(0).getIp();
                 intent.putExtra(ButtonDeckActivity.EXTRA_MODE, "0");
                    startActivity(intent);
                    break;
                default:
                    TextView textView2 = findViewById(R.id.statusTextView);
                    textView2.setText(getString(R.string.devices_found_multiple));

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setSingleChoiceItems(adapter, 0, (dialogInterface, i) -> {
                        NetworkDevice item = devices.get(i);

                        Toast.makeText(this, "Connecting to " + item.getDeviceName() + "!", Toast.LENGTH_LONG).show();

                        //Connect to the device
                        Intent intent2 = new Intent(this, ButtonDeckActivity.class);
                        intent2.putExtra(ButtonDeckActivity.EXTRA_IP, item.getIp());
                        mode_init_ip = item.getIp();
                        intent2.putExtra(ButtonDeckActivity.EXTRA_MODE, "0");
                        //intent2.putExtra(ButtonDeckActivity.EXTRA_MODE, "0");
                        dialogInterface.dismiss();
                        startActivity(intent2);
                    });
                    builder.setTitle(R.string.devices_found_multiple);
                    builder.setNeutralButton("Cancel", (dialogInterface, i) -> {
                    });
                    AlertDialog dialog = builder.create();

                    dialog.show();


                    break;
            }
            rescanButton.setVisibility(View.VISIBLE);
            getPreferences(MODE_PRIVATE).edit().putBoolean(autoScanPref, true).apply();
        });

    }
}
