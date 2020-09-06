package net.robertocpaes.displaybuttons;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import net.robertocpaes.displaybuttons.utils.Constants;
import net.robertocpaes.displaybuttons.utils.MySession;

import java.util.Arrays;
import java.util.List;

public class AdsMob extends AppCompatActivity {
    public InterstitialAd mInterstitialAd;
private MySession session;
    private BillingProcessor bp;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admob);




       updateADS();




        }
    private void updateADS(){
      //  mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
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

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2620537343731622/1249696477");
        //    startActivity(new Intent(AdsMob.this, MainActivity.class));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                  //  Toast.makeText(AdsMob.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                    // Code to be executed when an ad finishes loading.
                    mInterstitialAd.show();
                }

                @Override
                public void onAdClosed() {
                    startActivity(new Intent(AdsMob.this, MainActivity.class));
                }
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    startActivity(new Intent(AdsMob.this, MainActivity.class));

                }

            });













    }

    private void startGame() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.


        //retryButton.setVisibility(View.INVISIBLE);
       // resumeGame(GAME_LENGTH_MILLISECONDS);
    }
}


