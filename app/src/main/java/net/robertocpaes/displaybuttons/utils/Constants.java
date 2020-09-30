package net.robertocpaes.displaybuttons.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.LongSparseArray;

import net.robertocpaes.displaybuttons.AdsMob;
import net.robertocpaes.displaybuttons.ButtonDeckActivity;
import net.robertocpaes.displaybuttons.ConfigActivity;
import net.robertocpaes.displaybuttons.MainActivity;
import net.robertocpaes.displaybuttons.networking.INetworkPacket;
import net.robertocpaes.displaybuttons.networking.impl.AlternativeHelloPacket;
import net.robertocpaes.displaybuttons.networking.impl.ButtonInteractPacket;
import net.robertocpaes.displaybuttons.networking.impl.DesktopDisconnectPacket;
import net.robertocpaes.displaybuttons.networking.impl.DeviceIdentityPacket;
import net.robertocpaes.displaybuttons.networking.impl.HeartbeatPacket;
import net.robertocpaes.displaybuttons.networking.impl.HelloPacket;
import net.robertocpaes.displaybuttons.networking.impl.MatrizPacket;
import net.robertocpaes.displaybuttons.networking.impl.SingleUniversalChangePacket;
import net.robertocpaes.displaybuttons.networking.impl.SlotImageChangeChunkPacket;
import net.robertocpaes.displaybuttons.networking.impl.SlotImageClearChunkPacket;
import net.robertocpaes.displaybuttons.networking.impl.SlotImageClearPacket;
import net.robertocpaes.displaybuttons.networking.impl.SlotUniversalChangeChunkPacket;
import net.robertocpaes.displaybuttons.networking.impl.SlotLabelButtonClearChunkPacket;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Integer.valueOf;

/**
 * Created by NickAc on 24/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
public class Constants {
    public static ButtonDeckActivity buttonDeckContext;
    public static AdsMob AdsMobContext;
    public static final String SHARED_PREFS = "configPort";
    public static final String TEXT = "5095";
    public static MainActivity MainActivityContext;
    public static ConfigActivity ConfigActivityContext;
    public static String sharedPreferencesName = "ApplicationData";
    public static boolean isAdsIntroAlready = false;
    public static SharedPreferences sharedPreferences;
    public static final String LICENSE_KEY = "ApplicationData";
    public static String DEVICE_GUID_PREF = "device_guid";
    public static int PROTOCOL_VERSION = 12;
    public static LongSparseArray<INetworkPacket> packetMap = new LongSparseArray<>();

    public static int getMyPort(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return valueOf(sharedPreferences.getString(TEXT, "5095"));
    }
    static {
        registerPacket(new HelloPacket()); // 1
        registerPacket(new DeviceIdentityPacket()); //2
        registerPacket(new HeartbeatPacket()); // 3
        registerPacket(new DesktopDisconnectPacket()); //4
        registerPacket(new SingleUniversalChangePacket()); //5
        registerPacket(new AlternativeHelloPacket()); //6
        registerPacket(new SlotImageChangeChunkPacket()); //7
        registerPacket(new ButtonInteractPacket(ButtonInteractPacket.ButtonAction.NONE)); //8
        registerPacket(new SlotImageClearPacket()); //9
        registerPacket(new SlotImageClearChunkPacket()); //10
        registerPacket(new MatrizPacket()); //11
        registerPacket(new SlotUniversalChangeChunkPacket()); //12
        registerPacket(new SlotLabelButtonClearChunkPacket()); //13



     //   registerPacket(new UsbInteractPacket());
    }


    public static void registerPacket(INetworkPacket packet) {
        packetMap.append(packet.getPacketId(), packet);
    }

    public static INetworkPacket getNewPacket(long id) {
        Log.d("ButtonDeck", "Getting new packet for id " + id + ".");
        INetworkPacket packet = packetMap.get(id, null);
        return packet.clonePacket();
    }
}
