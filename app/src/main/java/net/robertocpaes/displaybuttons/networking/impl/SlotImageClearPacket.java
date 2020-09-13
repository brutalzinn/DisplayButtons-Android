package net.robertocpaes.displaybuttons.networking.impl;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import net.robertocpaes.displaybuttons.ButtonDeckActivity;
import net.robertocpaes.displaybuttons.networking.INetworkPacket;
import net.robertocpaes.displaybuttons.networking.io.ArchitectureAnnotation;
import net.robertocpaes.displaybuttons.networking.io.PacketArchitecture;
import net.robertocpaes.displaybuttons.networking.io.SocketServer;
import net.robertocpaes.displaybuttons.networking.io.TcpClient;
import net.robertocpaes.displaybuttons.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by NickAc on 05/01/2018.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.CLIENT_TO_SERVER)
public class SlotImageClearPacket implements INetworkPacket {
    int slot = -1;

    public SlotImageClearPacket() {
    }

    public SlotImageClearPacket(int slot) {
        this.slot = slot;
    }

    @Override
    public void execute(TcpClient client, boolean received) {
        if (received) {
            if (Constants.buttonDeckContext != null) {
                int id = Constants.buttonDeckContext.getResources().getIdentifier("button" + slot, "id", Constants.buttonDeckContext.getPackageName());
                if (slot <= 0) return;
                if (Constants.buttonDeckContext != null)
                    Constants.buttonDeckContext.runOnUiThread(() -> {
                        //Log.i("ButtonDeck", "Finding ID!");

                        // ImageButton view = Constants.buttonDeckContext.findViewById(slot);
                        Button view = Constants.buttonDeckContext.getButtonByTag(slot);

                        if (view != null) {
                            //Log.i("ButtonDeck", "Setting button!");
                            view.setBackground(null);
                            view.setText("");
                            view.setGravity(0x00000000);
                        }
                    //    System.gc();
                    });
            }
        }
    }
    @Override
    public void execute(SocketServer client, boolean received) {
        if (received) {
            if (Constants.buttonDeckContext != null) {
                int id = Constants.buttonDeckContext.getResources().getIdentifier("button" + slot, "id", Constants.buttonDeckContext.getPackageName());
                if (slot <= 0) return;
                if (Constants.buttonDeckContext != null)
                    Constants.buttonDeckContext.runOnUiThread(() -> {
                        //Log.i("ButtonDeck", "Finding ID!");

                        // ImageButton view = Constants.buttonDeckContext.findViewById(slot);
                        Button view = Constants.buttonDeckContext.getButtonByTag(slot);

                        if (view != null) {
                            //Log.i("ButtonDeck", "Setting button!");

                           // view.setScaleType(ImageView.ScaleType.FIT_XY);
                            view.setBackground(null);
                            view.setText("");
                            view.setGravity(0x00000000);
                        }
                        System.gc();
                    });
            }
        }
    }
    @Override
    public INetworkPacket clonePacket() {
        return new SlotImageClearPacket();
    }

    @Override
    public long getPacketId() {
        return 9;
    }

    @Override
    public void toOutputStream(DataOutputStream writer) {

    }

    @Override
    public void fromInputStream(DataInputStream reader) throws IOException {
        slot = reader.readInt();
    }
}