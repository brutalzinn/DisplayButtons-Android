package net.robertocpaes.displaybuttons.networking.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
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

    private long finaljsontime ;


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
        int arrayLenght = reader.readInt();
        reader.readFully(imageBytes, 0, arrayLenght);
        String json =  reader.readUTF();

        /*if (numberRead != arrayLenght) {
            //Log.e("ButtonDeck", "The number of bytes read is different from the size of the array");
            return;
        }*/




        if (Constants.buttonDeckContext != null) {


            Thread th = new Thread(() -> {
                if (imageSlot <= 0) return;
                //Log.i("ButtonDeck", "Starting to decode the bitmap!");

                Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, arrayLenght);
                //Log.i("ButtonDeck", "Decode Complete!");



                Constants.buttonDeckContext.runOnUiThread(new Runnable() {

                    String  font= "";
                    String text = "";
                    int  size = 0;
                    int  position = 0;
                    String  color= "";

                    float  radius = 0 ;
                    float dx  = 0;
                    float  dy  = 0;
                    String shadow_color  = "";
                    boolean is_stroke = false;

                    boolean  is_bold = false;
                    boolean is_normal = false;
                    boolean is_hint = false;
                    boolean  is_italic;
                    public void run() {







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
                                if(is_italic){
                                    view.setTypeface(null, Typeface.ITALIC);
                                }
                                if(is_normal){
                                    view.setTypeface(null,Typeface.NORMAL);
                                }
                                if(is_hint){
                                    view.setAllCaps(true);
                                }else{
                                    view.setAllCaps(false);
                                }
                                if(is_bold){
                                    view.setTypeface(null,Typeface.BOLD);
                                }
                            }else{
                                view.setText(0);
                            }
                            //    view.setScaleType(ImageView.ScaleType.FIT_XY);
                            view.setBackground(new BitmapDrawable(Constants.buttonDeckContext.getResources(), bmp));

                        }
                        System.gc();
                    }
                });

            });

            th.start();

        }
    }


}
