package net.nickac.buttondeck.networking.impl;

import net.nickac.buttondeck.networking.INetworkPacket;
import net.nickac.buttondeck.networking.io.ArchitectureAnnotation;
import net.nickac.buttondeck.networking.io.PacketArchitecture;
import net.nickac.buttondeck.networking.io.SocketServer;
import net.nickac.buttondeck.networking.io.TcpClient;
import net.nickac.buttondeck.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static net.nickac.buttondeck.utils.Constants.DEVICE_GUID_PREF;
import static net.nickac.buttondeck.utils.Constants.sharedPreferences;

/**
 * Created by NickAc on 24/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.SERVER_TO_CLIENT)
public class UsbInteractPacket implements INetworkPacket {
    @Override
    public void execute(TcpClient client, boolean received) {
        if (received) {
            client.sendPacket(clonePacket());
        }
    }
    @Override
    public void execute_server(SocketServer client, boolean received) {
        if (received) {
            client.sendPacket(clonePacket());
        }
    }
    @Override
    public INetworkPacket clonePacket() {
        return new HelloPacket();
    }

    @Override
    public long getPacketId() {
        return 1;
    }

    @Override
    public void toOutputStream(DataOutputStream writer) throws IOException {
        //Write the protocol version
        writer.writeInt(Constants.PROTOCOL_VERSION);
        //Write if we have a Guid for identification
        boolean hasGuid = sharedPreferences != null && sharedPreferences.contains(DEVICE_GUID_PREF) && !sharedPreferences.getString(DEVICE_GUID_PREF, "").isEmpty();
        writer.writeBoolean(hasGuid);
        if (hasGuid) {
            writer.writeUTF(sharedPreferences.getString(DEVICE_GUID_PREF, ""));
        }
    }

    @Override
    public void fromInputStream(DataInputStream reader) throws IOException {

    }

}
