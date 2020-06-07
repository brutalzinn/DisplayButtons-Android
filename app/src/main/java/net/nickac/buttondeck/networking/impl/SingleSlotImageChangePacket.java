package net.nickac.buttondeck.networking.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import net.nickac.buttondeck.networking.INetworkPacket;
import net.nickac.buttondeck.networking.io.ArchitectureAnnotation;
import net.nickac.buttondeck.networking.io.PacketArchitecture;
import net.nickac.buttondeck.networking.io.SocketServer;
import net.nickac.buttondeck.networking.io.TcpClient;
import net.nickac.buttondeck.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by NickAc on 29/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.CLIENT_TO_SERVER)
public class SingleSlotImageChangePacket implements INetworkPacket {
    public static final int bytesLimit = 1024 * 50;


    @Override
    public void execute(TcpClient client, boolean received) {

    }
    @Override
    public void execute_server(SocketServer client, boolean received) {

    }

    @Override
    public INetworkPacket clonePacket() {
        return new SingleSlotImageChangePacket();
    }

    @Override
    public long getPacketId() {
        return 5;
    }

    @Override
    public void toOutputStream(DataOutputStream writer) {
        //Client to server
    }

    @Override
    public void fromInputStream(DataInputStream reader) throws IOException {
        //Server to client
        if (reader.readBoolean()) {
            readDeckImage(reader);
        }
    }

    private void readDeckImage(DataInputStream reader) throws IOException {
        byte[] imageBytes = new byte[bytesLimit];

        int imageSlot = reader.readInt();
        Log.i("ButtonDeck", "Findind ID!" + imageSlot);
        int arrayLenght = reader.readInt();
        reader.readFully(imageBytes, 0, arrayLenght);
        /*if (numberRead != arrayLenght) {
            //Log.e("ButtonDeck", "The number of bytes read is different from the size of the array");
            return;
        }*/
        if (Constants.buttonDeckContext != null) {
            //Start a new thread to create a bitmap
            //Log.i("ButtonDeck", "Starting a new thread to decode the bitmap!");
            Thread th = new Thread(() -> {

                //Log.i("ButtonDeck", "Starting to decode the bitmap!");
                Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, arrayLenght);
                //Log.i("ButtonDeck", "Decode Complete!");


               // int id = Constants.buttonDeckContext.getResources().getIdentifier("button" + imageSlot, "id", Constants.buttonDeckContext.getPackageName());
                if (imageSlot <= 0) return;
                Constants.buttonDeckContext.runOnUiThread(() -> {
                //  Log.i("ButtonDeck", "Findind ID!");

                    ImageButton view = Constants.buttonDeckContext.GetButtonImage(imageSlot);
                    if (view != null) {
                        ////Log.i("ButtonDeck", "Setting button!");

                        view.setScaleType(ImageView.ScaleType.FIT_XY);
                        view.setBackground(new BitmapDrawable(Constants.buttonDeckContext.getResources(), bmp));
                    }
                    System.gc();
                });

            });
            th.start();
        }
    }


}
