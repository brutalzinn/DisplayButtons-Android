package net.nickac.buttondeck.networking.impl;

import net.nickac.buttondeck.networking.INetworkPacket;
import net.nickac.buttondeck.networking.io.SocketServer;
import net.nickac.buttondeck.networking.io.TcpClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by NickAc on 27/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
public class HeartbeatPacket implements INetworkPacket {
    @Override
    public void execute(TcpClient client, boolean received) {

    }
    @Override
    public void execute_server(SocketServer client, boolean received) {

    }

    @Override
    public INetworkPacket clonePacket() {
        return new HeartbeatPacket();
    }

    @Override
    public long getPacketId() {
        return 3;
    }

    @Override
    public void toOutputStream(DataOutputStream writer) throws IOException {

    }

    @Override
    public void fromInputStream(DataInputStream reader) throws IOException {

    }
}
