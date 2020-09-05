package net.robertocpaes.displaybuttons;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import net.robertocpaes.displaybuttons.utils.Constants;
import net.robertocpaes.displaybuttons.utils.MySession;

public class AdsMob extends AppCompatActivity {
    public InterstitialAd mInterstitialAd;
private MySession session;
    private BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admob);
        mInterstitialAd = new InterstitialAd(this);


        session = new MySession(this);
        startActivity(new Intent(AdsMob.this, MainActivity.class));
        //    updateADS();




        }
    private void updateADS(){
      //  mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId("@string/admob_banner_id");
        //    startActivity(new Intent(AdsMob.this, MainActivity.class));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                  mInterstitialAd.show();
                    // Code to be executed when an ad finishes loading.
                }

                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    //mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    startActivity(new Intent(AdsMob.this, MainActivity.class));

                }

            });







    }
}


