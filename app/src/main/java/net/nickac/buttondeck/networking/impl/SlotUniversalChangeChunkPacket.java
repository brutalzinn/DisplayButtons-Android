package net.nickac.buttondeck.networking.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;




import net.nickac.buttondeck.networking.INetworkPacket;
import net.nickac.buttondeck.networking.io.ArchitectureAnnotation;
import net.nickac.buttondeck.networking.io.PacketArchitecture;
import net.nickac.buttondeck.networking.io.SocketServer;
import net.nickac.buttondeck.networking.io.TcpClient;
import net.nickac.buttondeck.utils.Constants;
import net.nickac.buttondeck.utils.Json;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


import static java.lang.String.valueOf;
import static net.nickac.buttondeck.networking.impl.MatrizPacket.can_start;

/**
 * Created by NickAc on 31/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.CLIENT_TO_SERVER)
public class SlotUniversalChangeChunkPacket implements INetworkPacket {
    private static final int bytesLimit = 1024 * 50;
public int deckCount_total = 0;
    public int deckCount_packets = 0;

    public  int slot;
    public  int arraylenght;
    public  byte [] internalbtpm = new byte[bytesLimit];
    public  String font;
    public  int size;
    public  String text;
    public  int position;
    public  String color;
    @Override
    public void execute(TcpClient client, boolean received) {

    }
    @Override
    public void execute(SocketServer client, boolean received) {






    }

    @Override
    public INetworkPacket clonePacket() {
        return new SlotUniversalChangeChunkPacket();
    }

    @Override
    public long getPacketId() {
        return 12;
    }

    @Override
    public void toOutputStream(DataOutputStream writer) throws IOException {
        writer.writeInt(1);
    }
    @Override
    public void fromInputStream(DataInputStream reader) throws IOException {
        if (Constants.buttonDeckContext != null) {
            int imagesToRead = reader.readInt();

            for (int i = 0; i < imagesToRead; i++) {

                try {
                    readDeckImage(reader);
                } catch (IOException ignored) {
                }
                if (imagesToRead < i) {

                    Constants.buttonDeckContext.server.sendPacket(new SlotUniversalChangeChunkPacket());
                }

            }
        }
    }


    private void readDeckImage(DataInputStream reader) throws IOException {
        byte[] imageBytes = new byte[bytesLimit];




        int imageSlot = reader.readInt();
        Log.i("ButtonDeck", "Findind ID!" + imageSlot);
        int arrayLenght = reader.readInt();
        reader.readFully(imageBytes, 0, arrayLenght);
        String json =  reader.readUTF();




        if (Constants.buttonDeckContext != null) {


            //Start a new thread to create a bitmap
            Thread th = new Thread(() -> {





                Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, arrayLenght);

                ///    int id = Constants.buttonDeckContext.getResources().getIdentifier("button" + imageSlot, "id", Constants.buttonDeckContext.getPackageName());
                if (imageSlot <= 0) return;
                Constants.buttonDeckContext.runOnUiThread(() -> {

                    try {

                        JSONObject my_obj = new JSONObject(json);

                        font = my_obj.getString("Font");
                        text = my_obj.getString("Text");
                        size = my_obj.getInt("Size");
                        position = my_obj.getInt("Position");
                        color = my_obj.getString("Color");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                Button view = Constants.buttonDeckContext.getButtonByTag(imageSlot);
                //  TextView button = Constants.buttonDeckContext.getTextViewyTag(labelSlot);
                    if (view != null) {
                    //    Log.d("DEbug", "MUDANDO LABEL PARA" + text + " NO ID: " + labelSlot);

                    if(color== null || color.length() == 0) {
                        Log.d("DEbug", "COR VINDO NULA:" + color);

                        view.setTextColor(Color.parseColor("#FFFFFF"));
                    }else{
                        Log.d("DEbug", "Mudando cor para :" + color);

                        view.setTextColor(Color.parseColor(color));
                    }

                        view.setTextSize(size);

                        view.setGravity(position);

view.setShadowLayer(2.6f,1.5f,1.3f,Color.parseColor("#FFFFFF"));
                  //      view.setPadding(0,pos,0,0);

                        view.setText(text);
                        view.setBackground(new BitmapDrawable(Constants.buttonDeckContext.getResources(), bmp));

                     //  view.setTextSize(size);
                      //view.setTextColor(color);


                    }

                });
            });
            th.start();
        }


    }

}
