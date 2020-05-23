package net.nickac.buttondeck.networking.io;

import net.nickac.buttondeck.networking.INetworkPacket;
import net.nickac.buttondeck.utils.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;

public class SocketServer {

    private static final int SERVER_PORT = 5095;

    private ServerSocket mSocketServer = null;
    BufferedWriter mServerWriter = null;
    BufferedReader mReaderFromClient = null;
    private final List<INetworkPacket> toDeliver = Collections.synchronizedList(new ArrayList<INetworkPacket>());
    boolean createNewThread = true;
    private UUID connectionUUID = UUID.randomUUID();
    private String ip;
    private int port;
    private ServerSocket server;
    private Socket internalSocket;
    private Thread internalThread;
    private Thread dataThread;
    private Thread dataDeliveryThread;
    private List<Runnable> eventConnected = new ArrayList<>();


    public void connect() throws IOException {
        if (createNewThread) {
            internalThread = new Thread(() -> {
                SocketServer();
            });
            internalThread.start();
        } else {
            SocketServer();
        }
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
                   //     packet.execute(this, true);
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
                        iNetworkPacket.execute_server(this, false);

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
    public UUID getConnectionUUID() {
        return connectionUUID;
    }
    public void onConnected(Runnable event) {
        eventConnected.add(event);
    }


    private void initSocket() throws IOException {



    }
    public void SocketServer() {

        try {

            mSocketServer = new ServerSocket(SERVER_PORT);

            System.out.println("connecting...");
            internalSocket = mSocketServer.accept();

            InputStream is = internalSocket.getInputStream();
            OutputStream os = internalSocket.getOutputStream();

            mReaderFromClient = new BufferedReader(new InputStreamReader(is));
            mServerWriter = new BufferedWriter(new OutputStreamWriter(os));

                dataThread = new Thread(this::readData);
                dataThread.start();
                dataDeliveryThread = new Thread(this::sendData);
                dataDeliveryThread.start();

        } catch (Exception e) {
            System.out.println("Fail to create socket.." + e.toString());
        }
        try {
     //       mServerWriter.close();
      //      mReaderFromClient.close();
       //     mSocketServer.close();
        } catch (Exception e) {
        }
    }


}