package net.nickac.buttondeck.networking.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
 * Created by NickAc on 31/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.CLIENT_TO_SERVER)
public class SlotImageChangeChunkPacket implements INetworkPacket {
    private static final int bytesLimit = 1024 * 50;

    @Override
    public void execute(TcpClient client, boolean received) {

    }
    @Override
    public void execute_server(SocketServer client, boolean received) {

    }

    @Override
    public INetworkPacket clonePacket() {
        return new SlotImageChangeChunkPacket();
    }

    @Override
    public long getPacketId() {
        return 7;
    }

    @Override
    public void toOutputStream(DataOutputStream writer) {

    }

    @Override
    public void fromInputStream(DataInputStream reader) throws IOException {
        int imagesToRead = reader.readInt();

        for (int i = 0; i < imagesToRead; i++) {
            try {
                readDeckImage(reader);
            } catch (IOException ignored) {
            }

        }
    }

    private void readDeckImage(DataInputStream reader) throws IOException {
        byte[] imageBytes = new byte[bytesLimit];

        int imageSlot = reader.readInt();
        int arrayLength = reader.readInt();
        reader.readFully(imageBytes, 0, arrayLength);


        if (Constants.buttonDeckContext != null) {
            //Start a new thread to create a bitmap
            Thread th = new Thread(() -> {
                Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, arrayLength);
                int id = Constants.buttonDeckContext.getResources().getIdentifier("button" + imageSlot, "id", Constants.buttonDeckContext.getPackageName());
                if (imageSlot <= 0) return;
                Constants.buttonDeckContext.runOnUiThread(() -> {
                    ImageButton view = Constants.buttonDeckContext.findViewById(imageSlot);
                    if (view != null) {
                        view.setScaleType(ImageView.ScaleType.FIT_XY);
                        view.setBackground(new BitmapDrawable(Constants.buttonDeckContext.getResources(), bmp));
                    }

                });
            });
            th.start();
        }
    }
}
