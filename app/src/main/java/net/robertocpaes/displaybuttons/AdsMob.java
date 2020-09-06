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
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import net.robertocpaes.displaybuttons.utils.Constants;
import net.robertocpaes.displaybuttons.utils.MySession;

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
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2620537343731622/4519374567");
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



            });













    }

    private void startGame() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.


        //retryButton.setVisibility(View.INVISIBLE);
       // resumeGame(GAME_LENGTH_MILLISECONDS);
    }
}


