package net.robertocpaes.displaybuttons.networking.impl;

import android.util.Log;

import net.robertocpaes.displaybuttons.networking.INetworkPacket;
import net.robertocpaes.displaybuttons.networking.io.ArchitectureAnnotation;
import net.robertocpaes.displaybuttons.networking.io.PacketArchitecture;
import net.robertocpaes.displaybuttons.networking.io.SocketServer;
import net.robertocpaes.displaybuttons.networking.io.TcpClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by NickAc on 05/01/2018.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.CLIENT_TO_SERVER)
public class ButtonInteractPacket implements INetworkPacket {
    int slotID = -1;
    ButtonAction action;

    public ButtonInteractPacket(int slotID, ButtonAction action) {
        this.slotID = slotID;
        this.action = action;
    }

    public ButtonInteractPacket(ButtonAction action) {
        this.action = action;
    }

    @Override
    public void execute(TcpClient client, boolean received) {

    }
    @Override
    public void execute(SocketServer client, boolean received) {

    }
    @Override
    public INetworkPacket clonePacket() {
        return new ButtonInteractPacket(ButtonAction.NONE);
    }

    @Override
    public long getPacketId() {
        return 8;
    }

    @Override
    public void toOutputStream(DataOutputStream writer) throws IOException {
        writer.writeInt(action.getId());
        writer.writeInt(slotID);
    Log.d("ButtonDeck", "Wrote interaction with " + slotID + ".");
    }

    @Override
    public void fromInputStream(DataInputStream reader) {

    }

    public enum ButtonAction {
        NONE(-1),
        BUTTON_CLICK(0),
        BUTTON_DOWN(1),
        BUTTON_UP(2);


        private int id;

        ButtonAction(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
