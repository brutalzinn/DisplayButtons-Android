package net.nickac.buttondeck.networking.impl;

import android.os.Build;
import android.util.Log;

import net.nickac.buttondeck.networking.INetworkPacket;
import net.nickac.buttondeck.networking.compat.GuidCompact;
import net.nickac.buttondeck.networking.io.SocketServer;
import net.nickac.buttondeck.networking.io.TcpClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static net.nickac.buttondeck.utils.Constants.DEVICE_GUID_PREF;
import static net.nickac.buttondeck.utils.Constants.sharedPreferences;

/**
 * Created by NickAc on 26/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
public class DeviceIdentityPacket implements INetworkPacket {
    static String deviceGuid;
    @Override
    public void execute(TcpClient client, boolean received) {
        if (received) {
            client.sendPacket(clonePacket());
        }
    }
    @Override
    public void execute(SocketServer client, boolean received) {
        if (received) {
            client.sendPacket(clonePacket());
        }
    }

    @Override
    public INetworkPacket clonePacket() {
        if (sharedPreferences != null)
            deviceGuid = sharedPreferences.getString(DEVICE_GUID_PREF, "-");
        return new DeviceIdentityPacket();
    }

    @Override
    public long getPacketId() {
        return 2;
    }

    @Override
    public void toOutputStream(DataOutputStream writer) throws IOException {
        //From client to server
        writer.writeUTF(deviceGuid == null ? "00000000-0000-0000-0000-000000000000" : deviceGuid);
        writer.writeUTF(Build.MANUFACTURER + " " + Build.MODEL);
    }

    @Override
    public void fromInputStream(DataInputStream reader) throws IOException {
        //From server to client
        //The server might send us a Guid if we didn't send one earlier

        //Thankfully, the server tells us if we are going to receive a Guid
        if (reader.readBoolean()) {
            //We are going to receive a Guid.
            GuidCompact guid = new GuidCompact(reader.readUTF());
            Log.d("DEVICE GUID: ", guid + "");
            //We have a guid. Now we should store it in a shared preference
            if (sharedPreferences != null) {
                sharedPreferences.edit().putString(DEVICE_GUID_PREF, guid.toString()).apply();
            }
        }
    }
}
