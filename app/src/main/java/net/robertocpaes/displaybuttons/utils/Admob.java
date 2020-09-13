package net.robertocpaes.displaybuttons.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.internal.ads.zzajz;

public class Admob {
    private AdView adView;

    public void Disable(){
        if (adView != null) {
            ViewGroup parent = (ViewGroup) adView.getParent();
            parent.removeView(adView);
            parent.invalidate();
        }
    }
    public void Enable(){
        if (adView != null) {
            ViewGroup parent = (ViewGroup) adView.getParent();
            parent.addView(adView);
parent.invalidate();
        }
    }


public void OnDestroy(){
    if (adView != null) {
        adView.destroy();
    }
}
    public void onPause(){
        if (adView != null) {
            adView.pause();
        }
    }
    public void OnResume(){
        if (adView != null) {
            adView.resume();
        }
    }
    public void loadBanner(FrameLayout view, Context context, Activity activity, String AdsId) {
        // Create an ad request.


        adView = new AdView(context);

        adView.setAdUnitId(AdsId);
        view.removeAllViews();
        view.addView(adView);

        AdSize adSize = getAdSize(view,context,activity);
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize(FrameLayout view,Context context,Activity activity) {

        // Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = view.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationBannerAdSizeWithWidth(context, adWidth);
    }

}
