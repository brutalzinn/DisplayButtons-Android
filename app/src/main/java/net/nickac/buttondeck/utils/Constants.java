package net.nickac.buttondeck.utils;

import android.content.SharedPreferences;
import android.util.Log;
import android.util.LongSparseArray;

import net.nickac.buttondeck.ButtonDeckActivity;
import net.nickac.buttondeck.networking.INetworkPacket;
import net.nickac.buttondeck.networking.impl.AlternativeHelloPacket;
import net.nickac.buttondeck.networking.impl.ButtonInteractPacket;
import net.nickac.buttondeck.networking.impl.DesktopDisconnectPacket;
import net.nickac.buttondeck.networking.impl.DeviceIdentityPacket;
import net.nickac.buttondeck.networking.impl.HeartbeatPacket;
import net.nickac.buttondeck.networking.impl.HelloPacket;
import net.nickac.buttondeck.networking.impl.MatrizPacket;
import net.nickac.buttondeck.networking.impl.SingleSlotUniversalChangePacket;
import net.nickac.buttondeck.networking.impl.SlotUniversalChangeChunkPacket;
import net.nickac.buttondeck.networking.impl.SlotUniversalClearChunkPacket;
import net.nickac.buttondeck.networking.impl.SlotUniversalClearPacket;

/**
 * Created by NickAc on 24/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
public class Constants {
    public static ButtonDeckActivity buttonDeckContext;
    public static String sharedPreferencesName = "ApplicationData";
    public static SharedPreferences sharedPreferences;

    public static String DEVICE_GUID_PREF = "device_guid";
    public static int PROTOCOL_VERSION = 12;
    public static int PORT_NUMBER = 5095;
    public static LongSparseArray<INetworkPacket> packetMap = new LongSparseArray<>();

    static {
        registerPacket(new HelloPacket()); //1
        registerPacket(new DeviceIdentityPacket()); //2
        registerPacket(new HeartbeatPacket()); //3
        registerPacket(new DesktopDisconnectPacket()); //4
        registerPacket(new AlternativeHelloPacket()); //5
        registerPacket(new ButtonInteractPacket(ButtonInteractPacket.ButtonAction.NONE)); //6
        registerPacket(new MatrizPacket()); //7
        registerPacket(new SlotUniversalChangeChunkPacket()); //8
        registerPacket(new SlotUniversalClearChunkPacket()); //9
        registerPacket(new SingleSlotUniversalChangePacket()); //10
        registerPacket(new SlotUniversalClearPacket()); //1


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
