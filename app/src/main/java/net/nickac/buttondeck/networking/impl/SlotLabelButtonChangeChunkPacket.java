package net.nickac.buttondeck.networking.impl;

import android.widget.TextView;

import net.nickac.buttondeck.networking.INetworkPacket;
import net.nickac.buttondeck.networking.io.ArchitectureAnnotation;
import net.nickac.buttondeck.networking.io.PacketArchitecture;
import net.nickac.buttondeck.networking.io.SocketServer;
import net.nickac.buttondeck.networking.io.TcpClient;
import net.nickac.buttondeck.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static net.nickac.buttondeck.networking.impl.MatrizPacket.can_start;

/**
 * Created by NickAc on 31/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.CLIENT_TO_SERVER)
public class SlotLabelButtonChangeChunkPacket implements INetworkPacket {


    @Override
    public void execute(TcpClient client, boolean received) {

    }
    @Override
    public void execute_server(SocketServer client, boolean received) {

    }

    @Override
    public INetworkPacket clonePacket() {
        return new SlotLabelButtonChangeChunkPacket();
    }

    @Override
    public long getPacketId() {
        return 12;
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

        int labelSlot = reader.readInt();
        String font = reader.readUTF();
        String text = reader.readUTF();
        int size = reader.readInt();
        int pos = reader.readInt();
        int color = reader.readInt();


        if (Constants.buttonDeckContext != null) {
            //Start a new thread to create a bitmap
            Thread th = new Thread(() -> {

                ///    int id = Constants.buttonDeckContext.getResources().getIdentifier("button" + imageSlot, "id", Constants.buttonDeckContext.getPackageName());
                if (labelSlot <= 0) return;
                Constants.buttonDeckContext.runOnUiThread(() -> {
                    // ImageButton view = Constants.buttonDeckContext.findViewById(imageSlot);
                    TextView view = Constants.buttonDeckContext.getTextViewyTag(labelSlot);
                    if (view != null) {
                    view.setText(text);

                        //view.setTextSize(size);
                       // view.setTextColor(color);


                    }

                });
            });
            th.start();
        }


    }

    private void readDeckLabel(DataInputStream reader) throws IOException {


        int labelSlot = reader.readInt();
        String font = reader.readUTF();
        String text = reader.readUTF();
        int size = reader.readInt();
        int pos = reader.readInt();
        int color = reader.readInt();


        if (Constants.buttonDeckContext != null) {
            //Start a new thread to create a bitmap
            Thread th = new Thread(() -> {
if(can_start == false) return;
            ///    int id = Constants.buttonDeckContext.getResources().getIdentifier("button" + imageSlot, "id", Constants.buttonDeckContext.getPackageName());
                if (labelSlot <= 0) return;
                Constants.buttonDeckContext.runOnUiThread(() -> {
                   // ImageButton view = Constants.buttonDeckContext.findViewById(imageSlot);
                    TextView view = Constants.buttonDeckContext.getTextViewyTag(labelSlot);
                    if (view != null) {
                       view.setText(text);

                       view.setTextSize(size);
                       view.setTextColor(color);


                    }

                });
            });
            th.start();
        }
    }
}
