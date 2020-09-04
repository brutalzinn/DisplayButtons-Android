package net.robertocpaes.displaybuttons.utils.networkscan;

import android.util.Log;

import net.robertocpaes.displaybuttons.utils.NickTuple;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by NickAc on 30/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
public class Pinger {

    private static final int NUMTHREADS = 50;


    public static List<NetworkDevice> getDevicesOnNetwork(String subnet) {

        if (DiscoverRunner.devices == null)
            DiscoverRunner.devices = new HashMap<>();
        if (!DiscoverRunner.devices.isEmpty()) DiscoverRunner.devices.clear();

        LinkedList<NickTuple<InetAddress, String>> resAddresses = new LinkedList<NickTuple<InetAddress, String>>();
        DiscoverRunner[] tasks = new DiscoverRunner[NUMTHREADS];

        Thread[] threads = new Thread[NUMTHREADS];


        //Create Tasks and treads
        for (int i = 0; i < NUMTHREADS; i++) {
            tasks[i] = new DiscoverRunner(subnet, 254 / NUMTHREADS * i, 254 / NUMTHREADS);
            threads[i] = new Thread(tasks[i]);
        }
        //Starts threads
        for (int i = 0; i < NUMTHREADS; i++) {
            threads[i].start();
        }

        for (int i = 0; i < NUMTHREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < NUMTHREADS; i++) {
            for (NickTuple<InetAddress, String> a : tasks[i].getResults()) {
                try {
                    a.setKey(InetAddress.getByName(a.getKey().getHostAddress()));
                } catch (UnknownHostException e) {
                }
                resAddresses.add(a);
                Log.d("DEBUG", a.getKey().getHostAddress());
           }

        }

        ArrayList<NetworkDevice> foundDev = new ArrayList<>(resAddresses.size());

        for (NickTuple<InetAddress, String> a : resAddresses) {
            foundDev.add(new NetworkDevice(a.getKey().getHostAddress(), a.getValue()));

        }


        return foundDev;
    }


}
