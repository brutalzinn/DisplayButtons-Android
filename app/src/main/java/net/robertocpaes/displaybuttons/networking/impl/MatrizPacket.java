package net.robertocpaes.displaybuttons.networking.impl;

import android.util.Log;

import net.robertocpaes.displaybuttons.MainActivity;
import net.robertocpaes.displaybuttons.networking.INetworkPacket;
import net.robertocpaes.displaybuttons.networking.io.ArchitectureAnnotation;
import net.robertocpaes.displaybuttons.networking.io.PacketArchitecture;
import net.robertocpaes.displaybuttons.networking.io.SocketServer;
import net.robertocpaes.displaybuttons.networking.io.TcpClient;
import net.robertocpaes.displaybuttons.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static java.lang.Integer.valueOf;

/**
 * Created by NickAc on 27/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.CLIENT_TO_SERVER)
public class MatrizPacket implements INetworkPacket {
    public static int  NUM_ROWS = 3;
    public static  int NUM_COLS = 5;
public static boolean can_start  = false;
    @Override
    public void execute(TcpClient client, boolean received) {
        if(received){
            client.sendPacket(clonePacket());

        }
    }
    @Override
    public void execute(SocketServer client, boolean received) {
if(received){
    client.sendPacket(clonePacket());

}
    }

    @Override
    public INetworkPacket clonePacket() {
        return new MatrizPacket();
    }

    @Override
    public long getPacketId() {
        return 11;
    }

    @Override
    public void toOutputStream(DataOutputStream writer) throws IOException {
writer.writeBoolean(true);
    }

    @Override
    public void fromInputStream(DataInputStream reader) throws IOException {
        NUM_ROWS = reader.readInt() ;
        NUM_COLS = reader.readInt()  ;
        if (Constants.buttonDeckContext != null) {
            //Start a new thread to create a bitmap
            Thread th = new Thread(() -> {


                Constants.buttonDeckContext.runOnUiThread(() -> {
                    //ButtonDeckActivity teste = new ButtonDeckActivity();
                  //  teste.populateButtons();
Log.d("DEBUG", "CRIANDO MATRIZ!!!!");
               Constants.buttonDeckContext.limpar();
                    Constants.buttonDeckContext.populateButtons(valueOf(MainActivity.mode_init));
can_start  = true;
                });
            });
            th.start();
        }


     //


    }
}
