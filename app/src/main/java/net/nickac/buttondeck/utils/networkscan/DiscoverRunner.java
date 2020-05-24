package net.nickac.buttondeck.utils.networkscan;

import net.nickac.buttondeck.networking.impl.AlternativeHelloPacket;
import net.nickac.buttondeck.networking.io.TcpClient;
import net.nickac.buttondeck.utils.Constants;
import net.nickac.buttondeck.utils.NickTuple;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by NickAc on 30/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
public class DiscoverRunner implements Runnable {
    public static HashMap<UUID, String> devices = new HashMap<>();
    private List<NickTuple<InetAddress, String>> results;

    private String subnet;
    private Integer startAdd;
    private Integer numAdds;

    public DiscoverRunner(String subnet, Integer start, Integer steps) {
        this.subnet = subnet;
        this.startAdd = start;
        this.numAdds = steps;
        results = new LinkedList<>();
    }

    public static NickTuple<Boolean, String> isPortOpen(final String ip, final int port, final int timeout) {

        try {
            //Log.i("ButtonDeck", "Trying to check IP: " + ip);
            TcpClient socket = new TcpClient(ip, port);
            socket.setCreateNewThread(false);
            socket.connect(timeout);
            socket.sendPacket(new AlternativeHelloPacket());
         socket.waitForDisconnection();
            return new NickTuple<>(true, devices.get(socket.getConnectionUUID()));
        } catch (Exception ex) {
            return new NickTuple<>(false, "");
        }
    }

    @Override
    public void run() {

        int timeout = 750;
        for (int i = startAdd; i < startAdd + numAdds; i++) {
            String host = subnet + "." + i;
            try {
                InetAddress a = InetAddress.getByName(host);
                NickTuple<Boolean, String> portOpen = isPortOpen(host, Constants.PORT_NUMBER, timeout);
                if (portOpen.getKey()) {
                    results.add(new NickTuple<>(a, portOpen.getValue()));
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public List<NickTuple<InetAddress, String>> getResults() {
        return results;
    }

}
