package net.robertocpaes.displaybuttons.networking.impl;

import net.robertocpaes.displaybuttons.networking.INetworkPacket;
import net.robertocpaes.displaybuttons.networking.io.ArchitectureAnnotation;
import net.robertocpaes.displaybuttons.networking.io.PacketArchitecture;
import net.robertocpaes.displaybuttons.networking.io.SocketServer;
import net.robertocpaes.displaybuttons.networking.io.TcpClient;
import net.robertocpaes.displaybuttons.utils.networkscan.DiscoverRunner;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by NickAc on 30/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.BOTH_WAYS)
public class AlternativeHelloPacket implements INetworkPacket {
    private String deviceName = "";

    @Override
    public void execute(TcpClient client, boolean received) {
        if (!received) return;
        DiscoverRunner.devices.put(client.getConnectionUUID(), deviceName);
        client.close();
    }

    @Override
    public void execute(SocketServer client, boolean received) {
        if (!received) return;
        DiscoverRunner.devices.put(client.getConnectionUUID(), deviceName);
        client.close();
    }
    @Override
    public INetworkPacket clonePacket() {
        return new AlternativeHelloPacket();
    }

    @Override
    public long getPacketId() {
        return 6;
    }

    @Override
    public void toOutputStream(DataOutputStream writer) {

    }

    @Override
    public void fromInputStream(DataInputStream reader) throws IOException {
        deviceName = reader.readUTF();

    }
}
