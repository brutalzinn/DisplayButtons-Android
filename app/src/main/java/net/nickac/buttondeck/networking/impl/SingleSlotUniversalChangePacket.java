package net.nickac.buttondeck.networking.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.Button;
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
public class SingleSlotUniversalChangePacket implements INetworkPacket {
    public static final int bytesLimit = 1024 * 50;


    @Override
    public void execute(TcpClient client, boolean received) {

    }
    @Override
    public void execute(SocketServer client, boolean received) {

    }

    @Override
    public INetworkPacket clonePacket() {
        return new SingleSlotUniversalChangePacket();
    }

    @Override
    public long getPacketId() {
        return 10;
    }

    @Override
    public void toOutputStream(DataOutputStream writer) {
        //Client to server
    }

    @Override
    public void fromInputStream(DataInputStream reader) throws IOException {
        //Server to client

            readDeckImage(reader);

    }

    private void readDeckImage(DataInputStream reader) throws IOException {

        byte[] imageBytes = new byte[bytesLimit];
        int arrayLenght = reader.readInt();
        reader.readFully(imageBytes, 0, arrayLenght);
        int labelSlot = reader.readInt();
        String font = reader.readUTF();
        String text = reader.readUTF();
        int size = reader.readInt();
        int pos = reader.readInt();
       String color = reader.readUTF();
        /*if (numberRead != arrayLenght) {
            //Log.e("ButtonDeck", "The number of bytes read is different from the size of the array");
            return;
        }*/
        if (Constants.buttonDeckContext != null) {
            //Start a new thread to create a bitmap
            //Log.i("ButtonDeck", "Starting a new thread to decode the bitmap!");
            Thread th = new Thread(() -> {
                Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, arrayLenght);





                //int id = Constants.buttonDeckContext.getResources().getIdentifier("button" + imageSlot, "id", Constants.buttonDeckContext.getPackageName());
                if (labelSlot <= 0) return;
                Constants.buttonDeckContext.runOnUiThread(() -> {
                //  Log.i("ButtonDeck", "Findind ID!");

                 //   ImageButton view = Constants.buttonDeckContext.findViewById(imageSlot);

                    Button view = Constants.buttonDeckContext.getButtonByTag(labelSlot);
                    if (view != null) {

                        view.setBackground(new BitmapDrawable(Constants.buttonDeckContext.getResources(), bmp));



                        Log.d("DEbug", "MUDANDO LABEL PARA" + text + " NO ID: " + labelSlot);

                        if(color == null || color.length() == 0) {
                            Log.d("DEbug", "COR VINDO NULA:" + color);

                            view.setTextColor(Color.parseColor("#FFFFFF"));
                        }else{
                            Log.d("DEbug", "Mudando cor para :" + color);

                            view.setTextColor(Color.parseColor(color));
                        }

                        view.setTextSize(size);

                        view.setGravity(pos);

                        view.setShadowLayer(2.6f,1.5f,1.3f,Color.parseColor("#FFFFFF"));
                        //      view.setPadding(0,pos,0,0);

                        view.setText(text);

                        //  view.setTextSize(size);
                        //view.setTextColor(color);








                           }
                    System.gc();
                });

            });
            th.start();
        }
    }


}
