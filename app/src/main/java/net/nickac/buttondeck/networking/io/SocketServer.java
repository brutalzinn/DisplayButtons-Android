package net.nickac.buttondeck.networking.io;

import android.util.Log;

import net.nickac.buttondeck.networking.INetworkPacket;
import net.nickac.buttondeck.utils.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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

  private static int SERVER_PORT = 5095;

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
    private int timeout = 1500;

    public SocketServer(int port) {
        this.SERVER_PORT = port;
    }
    public void waitForDisconnection() throws InterruptedException, IOException {
       dataDeliveryThread.join();
      // internalSocket.close();
     //  connect();

    }
    public void sendPacket(INetworkPacket packet) {
        ArchitectureAnnotation annot = packet.getClass().getAnnotation(ArchitectureAnnotation.class);
        if (annot != null) {
           if (!(annot.value() == PacketArchitecture.CLIENT_TO_SERVER || annot.value() == PacketArchitecture.BOTH_WAYS)) {
  //  throw new IllegalStateException("Packet doesn't support being sent to the server.");
          }
        }
        toDeliver.add(packet);


    }
    public UUID getConnectionUUID() {
        return connectionUUID;
    }
    public void onConnected(Runnable event) {
        eventConnected.add(event);
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

                    mSocketServer = new ServerSocket(SERVER_PORT);

                    internalSocket = mSocketServer.accept();

                        SocketServer();










                } catch (IOException e) {
                }
            });
            internalThread.start();
        } else {
            SocketServer();
        }
    }

    private void sendData() {
        PrintStream outputStream;
        try {
            outputStream = new PrintStream(internalSocket.getOutputStream());

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

                       Log.i("ButtonDeck", "Written packet with ID " + iNetworkPacket.getPacketId() + ".");
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
                        //
                  Log.i("ButtonDeck", "read packet with ID " + packet.getPacketId() + ".");
                        packet.execute_server(this, true);

                    }

                } else {
                    Thread.sleep(50);
                }
            }
        } catch (InterruptedException e1) {
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Log.e("ButtonDeck", "ReadData stopped!");
    }
    public void close() {
        try {
            if (internalThread != null) internalThread.interrupt();
            if (dataThread != null) dataThread.interrupt();
            if (dataDeliveryThread != null) dataDeliveryThread.interrupt();
        // if (internalSocket != null) internalSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void SocketServer()  throws IOException {

        try {


        // PrintStream os = new PrintStream(internalSocket.getOutputStream());
      //  DataInputStream is = new DataInputStream(internalSocket.getInputStream());

        ;

//         Log.d("DEBUG", "Message Received: " + is.readLine());
          //  internalSocket.setSoTimeout(timeout);
           // internalSocket.setTcpNoDelay(true);
       //   internalSocket.connect(new InetSocketAddress(ip, port), timeout);

         for (Runnable r : eventConnected) {
              r.run();

         }

     dataThread = new Thread(this::readData);
   dataThread.start();
      dataDeliveryThread = new Thread(this::sendData);
   dataDeliveryThread.start();

        } catch (Exception e) {
            Log.d("DEBUG","fail to create a socket... " + e);
        }

        }




}