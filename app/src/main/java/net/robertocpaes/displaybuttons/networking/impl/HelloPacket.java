package net.robertocpaes.displaybuttons.networking.impl;

import net.robertocpaes.displaybuttons.networking.INetworkPacket;
import net.robertocpaes.displaybuttons.networking.io.ArchitectureAnnotation;
import net.robertocpaes.displaybuttons.networking.io.PacketArchitecture;
import net.robertocpaes.displaybuttons.networking.io.SocketServer;
import net.robertocpaes.displaybuttons.networking.io.TcpClient;
import net.robertocpaes.displaybuttons.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static net.robertocpaes.displaybuttons.utils.Constants.DEVICE_GUID_PREF;
import static net.robertocpaes.displaybuttons.utils.Constants.sharedPreferences;

/**
 * Created by NickAc on 24/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.SERVER_TO_CLIENT)
public class HelloPacket implements INetworkPacket {
    @Override
    public void execute(TcpClient client, boolean received) {

    }
    @Override
    public void execute(SocketServer client, boolean received) {

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
