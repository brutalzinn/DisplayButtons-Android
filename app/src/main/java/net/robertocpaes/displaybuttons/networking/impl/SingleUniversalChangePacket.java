package net.robertocpaes.displaybuttons.networking.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import net.robertocpaes.displaybuttons.networking.INetworkPacket;
import net.robertocpaes.displaybuttons.networking.io.ArchitectureAnnotation;
import net.robertocpaes.displaybuttons.networking.io.PacketArchitecture;
import net.robertocpaes.displaybuttons.networking.io.SocketServer;
import net.robertocpaes.displaybuttons.networking.io.TcpClient;
import net.robertocpaes.displaybuttons.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by NickAc on 29/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.CLIENT_TO_SERVER)
public class SingleUniversalChangePacket implements INetworkPacket {
    public static final int bytesLimit = 1024 * 50;
    public  String font;
    public  int size;
    public  String text;
    public  int position;
    public  String color;
    public float dx;
    public float dy;
    public float radius;
    public String shadow_color;
    public boolean is_stroke;
    public boolean is_italic;
    public boolean is_bold;
    public boolean is_normal;
    public boolean is_hint;
    @Override
    public void execute(TcpClient client, boolean received) {

    }
    @Override
    public void execute(SocketServer client, boolean received) {

    }

    @Override
    public INetworkPacket clonePacket() {
        return new SingleUniversalChangePacket();
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
        String json =  reader.readUTF();
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


                //int id = Constants.buttonDeckContext.getResources().getIdentifier("button" + imageSlot, "id", Constants.buttonDeckContext.getPackageName());
                if (imageSlot <= 0) return;

                Constants.buttonDeckContext.runOnUiThread(() -> {
                    try {

                        JSONObject my_obj = new JSONObject(json);

                        font = my_obj.getString("Font");
                        text = my_obj.getString("Text");
                        size = my_obj.getInt("Size");
                        position = my_obj.getInt("Position");
                        color = my_obj.getString("Color");
                        radius = (float) my_obj.getDouble("Stroke_radius");
                        dx = (float) my_obj.getDouble("Stroke_dx");
                        dy = (float) my_obj.getDouble("Stroke_dy");
                        shadow_color = my_obj.getString("Stroke_color");
                        is_stroke = my_obj.getBoolean("IsStroke");

                        is_bold = my_obj.getBoolean("Isboldtext");
                        is_normal = my_obj.getBoolean("Isnormaltext");
                        is_hint = my_obj.getBoolean("Ishinttext");
                        is_italic = my_obj.getBoolean("Isitalictext");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                //  Log.i("ButtonDeck", "Findind ID!");

                 //   ImageButton view = Constants.buttonDeckContext.findViewById(imageSlot);

                    Button view = Constants.buttonDeckContext.getButtonByTag(imageSlot);
                    if (view != null) {
                      Log.i("ButtonDeck", "Setting button!");
                        if(color == null || color.length() == 0) {
                            Log.d("DEbug", "COR VINDO NULA:" + color);

                            view.setTextColor(Color.parseColor("#FFFFFF"));
                        }else{
                            Log.d("DEbug", "Mudando cor para :" + color);

                            view.setTextColor(Color.parseColor(color));
                        }

                        view.setTextSize(size);

                        view.setGravity(position);

                        if(is_stroke) {
                            view.setShadowLayer(radius, dx, dy, Color.parseColor(shadow_color));
                        }else{
                            view.setShadowLayer(0,0,0,0);
                        }
                        if(text != null){
                            view.setText(text);
                        }else{
                            view.setText(0);
                        }
                    //    view.setScaleType(ImageView.ScaleType.FIT_XY);
                        view.setBackground(new BitmapDrawable(Constants.buttonDeckContext.getResources(), bmp));





                    }
                    System.gc();
                });

            });
            th.start();
        }
    }


}
