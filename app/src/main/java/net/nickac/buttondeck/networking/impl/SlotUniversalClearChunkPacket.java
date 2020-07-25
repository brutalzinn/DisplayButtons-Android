package net.nickac.buttondeck.networking.impl;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.ArrayList;
import java.util.List;

import static net.nickac.buttondeck.networking.impl.MatrizPacket.can_start;

/**
 * Created by NickAc on 06/01/2018.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
@ArchitectureAnnotation(PacketArchitecture.CLIENT_TO_SERVER)
public class SlotUniversalClearChunkPacket implements INetworkPacket {
    List<Integer> toClear = new ArrayList<>();

    @Override
    public void execute(TcpClient client, boolean received) {
        if (Constants.buttonDeckContext != null) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if(can_start == false) return;
                    for (int slot : toClear) {
                        TextView view = Constants.buttonDeckContext.getButtonByTag(slot);

                        if (view != null) {
                            Log.i("ButtonDeck", "Setting Label [CHUNK]!");
                            view.setText("");

                        }
                        System.gc();
                    }
                    synchronized (this) {
                        this.notify();
                    }
                }
            };
            synchronized (runnable) {
                Constants.buttonDeckContext.runOnUiThread(runnable);
                try {
                    runnable.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void execute(SocketServer client, boolean received) {
        if (Constants.buttonDeckContext != null) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if(can_start == false) return;
                    for (int slot : toClear) {
                        TextView view = Constants.buttonDeckContext.getButtonByTag(slot);

                        if (view != null) {
                    Log.i("ButtonDeck", "Setting Label [CHUNK]!");
                            view.setText("");

                        }
                        System.gc();
                    }
                    synchronized (this) {
                        this.notify();
                    }
                }
            };
            synchronized (runnable) {
                Constants.buttonDeckContext.runOnUiThread(runnable);
                try {
                    runnable.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public INetworkPacket clonePacket() {
        return new SlotUniversalClearChunkPacket();
    }

    @Override
    public long getPacketId() {
        return 13;
    }

    @Override
    public void toOutputStream(DataOutputStream writer) {

    }

    @Override
    public void fromInputStream(DataInputStream reader) throws IOException {
        int number = reader.readInt();
        for (int i = 0; i < number; i++) {
            toClear.add(reader.readInt());
        }
    }
}
