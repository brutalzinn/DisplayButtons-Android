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
    private  long finalbitmaptime ;
    private long bitmapinittime ;
    private long jsoninittime ;
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




        long packetreceivedtime =   System.nanoTime();
        int imageSlot = reader.readInt();
        int arrayLenght = reader.readInt();
        long packetreceivedbitmap =   System.nanoTime();
        reader.readFully(imageBytes, 0, arrayLenght);
        long finalpacketbitmape = System.nanoTime() - packetreceivedbitmap;
        long packetreceivedjson =   System.nanoTime();
        String json =  reader.readUTF();
        long finalppacketjson = System.nanoTime() - packetreceivedjson;
        long finalpackettime = System.nanoTime() - packetreceivedtime;
        /*if (numberRead != arrayLenght) {
            //Log.e("ButtonDeck", "The number of bytes read is different from the size of the array");
            return;
        }*/

        System.out.println("Getting bitmap packets with " + finalpacketbitmape / 1000000000  +" SEconds/ Ms:"+finalpacketbitmape/  1000000 + " "+  finalpacketbitmape + " NanoSeconds");
        System.out.println("Getting json button info with " + finalppacketjson / 1000000000 +" SEconds/ Ms:"+ finalpacketbitmape / 1000000 + " "+  finalpacketbitmape+ " NanoSeconds");
        System.out.println("Getting all button info with " + finalpackettime / 1000000000 +" SEconds/ Ms:" + finalpacketbitmape / 1000000 + " "+  finalpacketbitmape+ " NanoSeconds");
        if (Constants.buttonDeckContext != null) {
            final String[] text = {""};
            final String[] font = {""};
            final int[] size = {0};
            final int[] position = {0};
            final float[] dx = {0};
            final float[] dy = {0};
            final float[] radius = {0};
            final String[] shadow_color = {""};
            final boolean[] is_stroke = {false};
            final boolean[] is_italic = {false};
            final boolean[] is_bold = {false};
            final boolean[] is_normal = {false};
            final boolean[] is_hint = {false};
            final String[] color = {""};

            Thread th = new Thread(() -> {
                if (imageSlot <= 0) return;
                //Log.i("ButtonDeck", "Starting to decode the bitmap!");
                long bitmapinittime =   System.nanoTime();
                Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, arrayLenght);
                //Log.i("ButtonDeck", "Decode Complete!");
                finalbitmaptime = System.nanoTime() - bitmapinittime;
                jsoninittime =   System.nanoTime();
                try {

                    JSONObject my_obj = new JSONObject(json);

                    font[0] = my_obj.getString("Font");
                    text[0] = my_obj.getString("Text");
                    size[0] = my_obj.getInt("Size");
                    position[0] = my_obj.getInt("Position");
                    color[0] = my_obj.getString("Color");
                    radius[0] = (float) my_obj.getDouble("Stroke_radius");
                    dx[0] = (float) my_obj.getDouble("Stroke_dx");
                    dy[0] = (float) my_obj.getDouble("Stroke_dy");
                    shadow_color[0] = my_obj.getString("Stroke_color");
                    is_stroke[0] = my_obj.getBoolean("IsStroke");

                    is_bold[0] = my_obj.getBoolean("Isboldtext");
                    is_normal[0] = my_obj.getBoolean("Isnormaltext");
                    is_hint[0] = my_obj.getBoolean("Ishinttext");
                    is_italic[0] = my_obj.getBoolean("Isitalictext");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                long finaljsontime = System.nanoTime() - jsoninittime;


                Constants.buttonDeckContext.runOnUiThread(() -> {
                    long time = System.nanoTime();

                    Button view = Constants.buttonDeckContext.getButtonByTag(imageSlot);
                    if (view != null) {
                        Log.i("ButtonDeck", "Setting button!");
                        if(color[0] == null || color[0].length() == 0) {
                            Log.d("DEbug", "COR VINDO NULA:" + color[0]);

                            view.setTextColor(Color.parseColor("#FFFFFF"));
                        }else{
                            Log.d("DEbug", "Mudando cor para :" + color[0]);

                            view.setTextColor(Color.parseColor(color[0]));
                        }

                        view.setTextSize(size[0]);

                        view.setGravity(position[0]);

                        if(is_stroke[0]) {
                            view.setShadowLayer(radius[0], dx[0], dy[0], Color.parseColor(shadow_color[0]));
                        }else{
                            view.setShadowLayer(0,0,0,0);
                        }
                        if(text[0] != null){
                            view.setText(text[0]);
                            if(is_italic[0]){
                                view.setTypeface(null, Typeface.ITALIC);
                            }
                            if(is_normal[0]){
                                view.setTypeface(null,Typeface.NORMAL);
                            }
                            if(is_hint[0]){
                                view.setAllCaps(true);
                            }else{
                                view.setAllCaps(false);
                            }
                            if(is_bold[0]){
                                view.setTypeface(null,Typeface.BOLD);
                            }
                        }else{
                            view.setText(0);
                        }
                        //    view.setScaleType(ImageView.ScaleType.FIT_XY);
                        view.setBackground(new BitmapDrawable(Constants.buttonDeckContext.getResources(), bmp));



                        long finaltime = System.nanoTime() - time;


                        System.out.println("Setting button with " + finaltime / 1000000000 +" SEconds/ Ms:" + finaltime / 1000000 + " "+  finaltime+ " NanoSeconds");
                        System.out.println("Setting bitmap with " + finalbitmaptime / 1000000000 +" SEconds/ Ms:" + finalbitmaptime / 1000000 + " "+  finalbitmaptime+ " NanoSeconds");
                        System.out.println("Setting json with " + finaljsontime / 1000000000 +" SEconds/ Ms:" + finaljsontime / 1000000 + " "+  finaljsontime+ " NanoSeconds" );
                    }
                    System.gc();
                });
            });

            th.start();

        }
    }


}
