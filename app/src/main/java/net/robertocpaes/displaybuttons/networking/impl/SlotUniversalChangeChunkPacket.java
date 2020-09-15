package net.robertocpaes.displaybuttons.networking.impl;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.fonts.Font;
import android.os.Build;
import android.provider.CalendarContract;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;




import net.robertocpaes.displaybuttons.networking.INetworkPacket;
import net.robertocpaes.displaybuttons.networking.io.ArchitectureAnnotation;
import net.robertocpaes.displaybuttons.networking.io.PacketArchitecture;
import net.robertocpaes.displaybuttons.networking.io.SocketServer;
import net.robertocpaes.displaybuttons.networking.io.TcpClient;
import net.robertocpaes.displaybuttons.utils.Constants;
import net.robertocpaes.displaybuttons.utils.Json;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


import static java.lang.String.valueOf;
import static net.robertocpaes.displaybuttons.networking.impl.MatrizPacket.can_start;

/**
 * Created by NickAc on 31/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.CLIENT_TO_SERVER)
public class SlotUniversalChangeChunkPacket implements INetworkPacket  {
    private static final int bytesLimit = 1024 * 50;
public int deckCount_total = 0;
    public int deckCount_packets = 0;

    public  int slot;
    public  int arraylenght;
    public  byte [] internalbtpm = new byte[bytesLimit];

    private  long finalbitmaptime ;
    private long bitmapinittime ;
    private long jsoninittime ;
    private long finaljsontime ;
    private long finaltime;


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
            int i = 0;
            for ( i = 0; i < imagesToRead; i++) {

                try {
                    readDeckImage(reader);
                } catch (IOException ignored) {
                }


            }
            if( i == imagesToRead){
                System.out.println("Setting ALL button with " + finaltime / 1000000000 +" SEconds/ Ms:" + finaltime / 1000000 + " "+  finaltime+ " NanoSeconds");

            }
        }
    }


    private void readDeckImage(DataInputStream reader)   throws IOException {
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
                long bitmapinittime =   System.nanoTime();
                Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, arrayLenght);
                //Log.i("ButtonDeck", "Decode Complete!");
                finalbitmaptime = System.nanoTime() - bitmapinittime;
                jsoninittime =   System.nanoTime();

                long finaljsontime = System.nanoTime() - jsoninittime;

                Constants.buttonDeckContext.runOnUiThread(new Runnable() {
                    long time = System.nanoTime();
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



                        long finaltime = System.nanoTime() - time;


                        System.out.println("Setting All buttons with " + finaltime / 1000000000 +" SEconds/ Ms:" + finaltime / 1000000 + " "+  finaltime+ " NanoSeconds");

                    }
                    System.gc();
                    }
                });

            });

            th.start();

        }
    }

}
