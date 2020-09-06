package net.robertocpaes.displaybuttons;

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
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import net.robertocpaes.displaybuttons.networking.io.SocketServer;
import net.robertocpaes.displaybuttons.utils.Constants;
import net.robertocpaes.displaybuttons.utils.networkscan.NetworkDevice;
import net.robertocpaes.displaybuttons.utils.networkscan.NetworkDeviceAdapter;
import net.robertocpaes.displaybuttons.utils.networkscan.NetworkSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.sql.DriverManager.println;

public class MainActivity extends AppCompatActivity {
   public String autoScanPref = "didAutoScan";
    public static final String EXTRA_MODE = "0";
    public static String mode_init = "0";
    public static String mode_init_ip = "127.0.0.1";
    private AdView adView;


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
        setContentView(R.layout.activity_principal_menu);
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
        adView = findViewById(R.id.ad_view_mainactivity);

        // Create an ad request.
        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.

        adView.loadAd(adRequest);


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



            }


        //    devices.
        // Toast.makeText(this, "Connecting to " + devices.get(0).getDeviceName() + "!", Toast.LENGTH_LONG).show();
        //   Toast.makeText(getApplicationContext(), "Connecting por usb !", Toast.LENGTH_LONG).show();
        // intent.putExtra(ButtonDeckActivity.EXTRA_IP, "127.0.0.1");
        //               startActivity(intent);





    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (adView != null) {
            adView.destroy();
        }
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

        // values/strings.xml.

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
