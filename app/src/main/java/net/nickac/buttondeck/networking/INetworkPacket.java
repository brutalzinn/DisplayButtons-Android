package net.nickac.buttondeck.networking;

import net.nickac.buttondeck.networking.io.SocketServer;
import net.nickac.buttondeck.networking.io.TcpClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by NickAc on 23/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
public interface INetworkPacket {
    void execute(TcpClient client, boolean received);
    void execute_server(SocketServer client, boolean received);
    INetworkPacket clonePacket();

    long getPacketId();

    void toOutputStream(DataOutputStream writer) throws IOException;

    void fromInputStream(DataInputStream reader) throws IOException;
}
