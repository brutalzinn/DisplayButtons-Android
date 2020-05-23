package net.nickac.buttondeck.networking.impl;

import net.nickac.buttondeck.networking.INetworkPacket;
import net.nickac.buttondeck.networking.io.ArchitectureAnnotation;
import net.nickac.buttondeck.networking.io.PacketArchitecture;
import net.nickac.buttondeck.networking.io.SocketServer;
import net.nickac.buttondeck.networking.io.TcpClient;
import net.nickac.buttondeck.utils.networkscan.DiscoverRunner;

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
    public void execute_server(SocketServer client, boolean received) {
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
