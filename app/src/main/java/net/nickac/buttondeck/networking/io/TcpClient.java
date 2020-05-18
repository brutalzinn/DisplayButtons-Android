package net.nickac.buttondeck.networking.io;

import net.nickac.buttondeck.networking.INetworkPacket;
import net.nickac.buttondeck.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by NickAc on 26/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
public class TcpClient {
    private final List<INetworkPacket> toDeliver = Collections.synchronizedList(new ArrayList<INetworkPacket>());
    boolean createNewThread = true;
    private UUID connectionUUID = UUID.randomUUID();
    private String ip;
    private int port;
    private Socket internalSocket;
    private Thread internalThread;
    private Thread dataThread;
    private Thread dataDeliveryThread;
    private List<Runnable> eventConnected = new ArrayList<>();
    private int timeout = 1500;

    public TcpClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public UUID getConnectionUUID() {
        return connectionUUID;
    }

    public void waitForDisconnection() throws InterruptedException {
        dataDeliveryThread.join();
    }

    public boolean isCreateNewThread() {
        return createNewThread;
    }

    public void setCreateNewThread(boolean createNewThread) {
        this.createNewThread = createNewThread;
    }

    public void connect(int timeout) throws IOException {
        this.timeout = timeout;
        connect();
    }

    public void connect() throws IOException {
        if (createNewThread) {
            internalThread = new Thread(() -> {
                try {
                    initSocket();
                } catch (IOException e) {
                }
            });
            internalThread.start();
        } else {
            initSocket();
        }
    }

    public void onConnected(Runnable event) {
        eventConnected.add(event);
    }

    public void sendPacket(INetworkPacket packet) {
        ArchitectureAnnotation annot = packet.getClass().getAnnotation(ArchitectureAnnotation.class);
        if (annot != null) {
            if (!(annot.value() == PacketArchitecture.CLIENT_TO_SERVER || annot.value() == PacketArchitecture.BOTH_WAYS)) {
                throw new IllegalStateException("Packet doesn't support being sent to the server.");
            }
        }
        toDeliver.add(packet);


    }

    private void readData() {
        List<Byte> readBytes = new ArrayList<>();
        DataInputStream inputStream;
        try {
            inputStream = new DataInputStream(internalSocket.getInputStream());
            while (internalSocket != null && internalSocket.isConnected()) {
                if (inputStream.available() > 0) {
                    long packetNumber = inputStream.readLong();
                    INetworkPacket packet = Constants.getNewPacket(packetNumber);
                    if (packet != null) {
                        packet.fromInputStream(inputStream);
                        packet.execute(this, true);
                    }

                } else {
                    Thread.sleep(50);
                }
            }
        } catch (InterruptedException e1) {
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ////Log.e("ButtonDeck", "ReadData stopped!");
    }

    private void sendData() {
        DataOutputStream outputStream;
        try {
            outputStream = new DataOutputStream(internalSocket.getOutputStream());

            while (internalSocket != null && internalSocket.isConnected()) {
                if (toDeliver.size() < 1) {
                    Thread.sleep(50);
                    continue;
                }
                synchronized (toDeliver) {
                    Iterator<INetworkPacket> iter = toDeliver.iterator();

                    while (iter.hasNext()) {
                        INetworkPacket iNetworkPacket = iter.next();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        DataOutputStream stream = new DataOutputStream(baos);

                        //Log.i("ButtonDeck", "Written packet with ID " + iNetworkPacket.getPacketId() + ".");
                        stream.writeLong(iNetworkPacket.getPacketId());
                        iNetworkPacket.toOutputStream(stream);

                        outputStream.write(baos.toByteArray());
                        outputStream.flush();
                        iNetworkPacket.execute(this, false);

                        stream.close();
                        baos.close();
                        iter.remove();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
        }
    }

    public void close() {
        try {
            if (internalThread != null) internalThread.interrupt();
            if (dataThread != null) dataThread.interrupt();
            if (dataDeliveryThread != null) dataDeliveryThread.interrupt();
            if (internalSocket != null) internalSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSocket() throws IOException {
        internalSocket = new Socket();
        internalSocket.connect(new InetSocketAddress(ip, port), timeout);
        for (Runnable r : eventConnected) {
            r.run();
        }

        dataThread = new Thread(this::readData);
        dataThread.start();
        dataDeliveryThread = new Thread(this::sendData);
        dataDeliveryThread.start();
    }
}
