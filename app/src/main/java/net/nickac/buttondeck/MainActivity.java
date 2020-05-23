package net.nickac.buttondeck;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        setContentView(R.layout.activity_main);
        Button rescanButton = findViewById(R.id.rescanButton);

        rescanButton.setOnClickListener(view -> scanDevices());

        TextView textView = findViewById(R.id.protocolVersionTextView);
        textView.setText(textView.getText().toString().replace("{0}", String.valueOf(Constants.PROTOCOL_VERSION)));

        boolean alreadyScanned = getPreferences(MODE_PRIVATE).getBoolean(autoScanPref, false);
        rescanButton.setVisibility(!alreadyScanned ? View.INVISIBLE : View.VISIBLE);

        if (!alreadyScanned) {
    //        scanDevices();
        }
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //SocketServer teste = new SocketServer();
                  //  teste.connect();
                    Intent intent = new Intent(getApplicationContext(), ButtonDeckActivity.class);


                    startActivity(intent);

                    //Your code goes here
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
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
