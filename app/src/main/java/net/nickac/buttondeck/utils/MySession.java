package net.nickac.buttondeck.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySession {

    Context context;
    private String ISPurchase = "IsPurchase";

    public MySession(Context context){

        this.context = context;
    }

    public void setUserPurchased(boolean isPurchase){


        if(context != null){

            SharedPreferences LoginPref = context.getSharedPreferences("inAppPrefs",context.MODE_PRIVATE);
      SharedPreferences.Editor editor = LoginPref.edit();
      editor.putBoolean(ISPurchase, isPurchase);
      editor.apply();
        }
    }
    public boolean isUserPurchased(){
        if (context != null){

            SharedPreferences LoginPref = context.getSharedPreferences("inAppPrefs",context.MODE_PRIVATE);
      return LoginPref.getBoolean(ISPurchase, false);
        }else{
            return false;
        }
    }


}
